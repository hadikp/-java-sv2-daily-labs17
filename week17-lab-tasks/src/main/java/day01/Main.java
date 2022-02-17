package day01;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUserName("root");
            dataSource.setPassword("ciprush11");
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Can't reach database!", sqe);
        }

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        //actorsRepository.saveActor("Jack Doe");
        System.out.println(actorsRepository.findActorsWithPrefix("Ja"));


    }
}
