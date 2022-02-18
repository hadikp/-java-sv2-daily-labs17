package activitytracker;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class ActivityTrackerMain {

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/activitytracker?useUnicode=true");
            dataSource.setUser("activity");
            dataSource.setPassword("activity");
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Can't reach database!", sqe);
        }

        ActivityRepo activityRepo = new ActivityRepo(dataSource);
        //activityRepo.saveActivity(LocalDateTime.of(2022, 02, 11, 10, 11, 11), "Futás", Type.RUNNING);
        //activityRepo.saveActivity(LocalDateTime.of(2022, 01, 21, 8, 33, 22), "Hosszű Futás", Type.RUNNING);
        activityRepo.saveActivity(LocalDateTime.of(2022, 01, 30, 11, 10, 11), "kosárlabda", Type.BASKETBALL);
        System.out.println(activityRepo.findAllActivity());


    }
}
