package day01;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorsRepository {

    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveActor(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("Insert into actors(actor_name) values (?)")){
            stmt.setString(1, name);
            stmt.executeUpdate();

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
}
