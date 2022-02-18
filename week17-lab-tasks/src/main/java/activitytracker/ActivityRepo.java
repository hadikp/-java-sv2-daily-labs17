package activitytracker;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

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
}
