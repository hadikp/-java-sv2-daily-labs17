package day02;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ActorsMoviesService {

    private ActorsRepo actorsRepo;
    private MoviesRepo moviesRepo;
    private ActorsMoviesRepo actorsMoviesRepo;

    public ActorsMoviesService(ActorsRepo actorsRepo, MoviesRepo moviesRepo, ActorsMoviesRepo actorsMoviesRepo) {
        this.actorsRepo = actorsRepo;
        this.moviesRepo = moviesRepo;
        this.actorsMoviesRepo = actorsMoviesRepo;
    }

    public void insertMovieWithActors(String title, LocalDate releseDate, List<String> actorNames){
        long movieId = moviesRepo.saveMovie(title, releseDate);
        for (String actual: actorNames) {
            long actorId;
            Optional<Actor> found = actorsRepo.findActorByName(actual);
            if(found.isPresent()) {
                actorId = found.get().getId();
            } else {
                actorId = actorsRepo.saveActor(actual);
            }
            actorsMoviesRepo.insertActorAndMovieId(actorId, movieId);
        }
    }
}
