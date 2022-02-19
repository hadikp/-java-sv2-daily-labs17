package day02;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MoviesRepo {

    private DataSource dataSource;

    public MoviesRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long  saveMovie(String title, LocalDate releaseDate) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement("insert into movies(title, release_date) values(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, title);
            statement.setDate(2, Date.valueOf(releaseDate));
            statement.executeUpdate();
            try(ResultSet rs = statement.getGeneratedKeys()) {
                if(rs.next()){
                    return rs.getLong(1);
                }
                throw new IllegalStateException("Insert failed to movies!");
            }
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Cannot connect!", sqe);
        }
    }

    public List<Movie> findAllMovies() {
        List<Movie> result = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("Select * FROM movies");
            ResultSet rs = stmt.executeQuery()) {
            processResultSet(result, rs);
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Cannot reach databse!", sqe);
        }
        return result;
    }

    private void processResultSet(List<Movie> result, ResultSet rs) throws SQLException {
        while (rs.next()) {
            Long id = rs.getLong("id");
            String title = rs.getString("title");
            LocalDate date = rs.getDate("release_date").toLocalDate();
            result.add(new Movie(id, title, date));
        }
    }
}
