package day02;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingsRepo {

    private DataSource dataSource;

    public RatingsRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRating(long movieId, List<Integer> rating) {
        try(Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try(PreparedStatement stmt = conn.prepareStatement("Insert into ratings(movie_id, rating) values(? ,?)")) {
                for(Integer actual: rating) {
                    if (actual < 1 || actual > 5) {
                        throw new IllegalArgumentException("Invalid rating!");
                    }
                    stmt.setLong(1, movieId);
                    stmt.setLong(2, actual);
                    stmt.executeUpdate();
                }
                conn.commit();
            }
            catch (IllegalArgumentException iae) {
                conn.rollback();
            }
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Cannot to ratings!", sqe);
        }
    }

    public void insertAverageRating(MovieWithRatings mwr) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("Update movies SET average_rate = ? where id = ?")) {
            stmt.setDouble(1, mwr.countListAverage());
            stmt.setLong(2, mwr.getMovieId());
            stmt.executeUpdate();
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Update failed into movies table!", sqe);
        }
    }

    public MovieWithRatings findRatingsByMovie(String title) {
        MovieWithRatings result = null;
        List<Long> ratingsList = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("Select * from ratings Join movies On ratings.movie_id = movies.id");
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String movieTitle = rs.getString("movies.title");
                Long rating = rs.getLong("ratings.rating");

                if(movieTitle.equals(title)) {
                    ratingsList.add(rating);
                    Long id = rs.getLong("movies.id");
                    result = new MovieWithRatings(id, ratingsList);
                }
            }
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Can't query!", sqe);
        }
        return result;
    }
}
