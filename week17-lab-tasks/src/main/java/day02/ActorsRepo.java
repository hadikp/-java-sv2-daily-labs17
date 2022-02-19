package day02;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActorsRepo {

    private DataSource dataSource;

    public ActorsRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long saveActor(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("Insert into actors(actor_name) values (?)", Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, name);
            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()){
                    return rs.getLong(1);
                }
                throw new IllegalStateException("Insert failed to movies!");
            }

        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Can't connect database!", sqe);
        }
    }

    public List<String> findActorsWithPrefix(String prefix) {
        List<String> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("Select actor_name from actors where actor_name like ?"))  {
            stmt.setString(1, prefix + "%");

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String actorName = rs.getString("actor_name");
                    result.add(actorName);
                }
            }
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Can't query!", sqe);
        }
        return result;
    }

    public Optional<Actor> findActorByName(String name) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("Select * from actors Where actor_name = ?")){
            stmt.setString(1, name);
            return makeSelect(stmt);
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Can't query!", sqe);
        }
    }

    private Optional<Actor> makeSelect(PreparedStatement stmt) throws SQLException {
        try(ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String actorName = rs.getString("actor_name");
                return Optional.of(new Actor(id, actorName));
            }
        }
        //throw new IllegalArgumentException("Don't find actor!");
        return Optional.empty();
    }
}
