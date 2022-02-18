package activitytracker;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityRepo {

    private DataSource dataSource;

    public ActivityRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveActivity(LocalDateTime startTime, String description, Type type) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement("Insert into activities(start_time, description, activity_type) values(?, ?, ?)")) {
            statement.setTimestamp(1, Timestamp.valueOf(startTime));
            statement.setString(2, description);
            statement.setString(3, type.name());
            statement.executeUpdate();
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Cant connection Database!", sqe);
        }
    }

    public List<Activity> findAllActivity() {
        List<Activity> result = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("Select * from activities");
            ResultSet rs = stmt.executeQuery()) {
            makeResultset(result, rs);
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Can't reach database!", sqe);
        }
        return result;
    }

    private void makeResultset(List<Activity> result, ResultSet rs) throws SQLException {
        while (rs.next()) {
            int id = rs.getInt("id");
            LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
            String description = rs.getString("description");
            String typeString = rs.getString("activity_type");
            Type type = Type.valueOf(typeString);
            result.add(new Activity(id, startTime, description, type));
        }
    }
}
