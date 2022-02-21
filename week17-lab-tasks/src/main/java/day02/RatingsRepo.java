package day02;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
}
