package day02;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("ciprush11");
        }
        catch (SQLException sqe) {
            throw new IllegalStateException("Can't reach database!", sqe);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        //flyway.clean();
        flyway.migrate();

        ActorsRepo actorsRepo = new ActorsRepo(dataSource);
        //actorsRepo.saveActor("Jack Doe");
        //System.out.println(actorsRepo.findActorsWithPrefix("Ja"));
        MoviesRepo moviesRepo = new MoviesRepo(dataSource);
        System.out.println(actorsRepo.findActorByName("John Doe"));

        ActorsMoviesRepo actorsMoviesRepo = new ActorsMoviesRepo(dataSource);
        RatingsRepo ratingsRepo = new RatingsRepo(dataSource);

        ActorsMoviesService service = new ActorsMoviesService(actorsRepo, moviesRepo, actorsMoviesRepo);
        MoviesRatingService moviesRatingService = new MoviesRatingService(moviesRepo, ratingsRepo);
        service.insertMovieWithActors("Titanic", LocalDate.of(1999, 01, 12), List.of("Leonardo", "Kate"));
        service.insertMovieWithActors("Terminátor", LocalDate.of(2001, 12, 21), List.of("Schwarcenegger", "Linda Hamilton"));

        //moviesRatingService.addRatings("Titanic", 2, 3, 4);
        moviesRatingService.addRatings("Terminátor", 2, 3, 4);

    }
}
