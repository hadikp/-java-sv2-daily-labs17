package day02;

import java.util.Arrays;
import java.util.Optional;

public class MoviesRatingService {

    private MoviesRepo moviesRepo;
    private RatingsRepo ratingsRepo;

    public MoviesRatingService(MoviesRepo moviesRepo, RatingsRepo ratingsRepo) {
        this.moviesRepo = moviesRepo;
        this.ratingsRepo = ratingsRepo;
    }

    public void addRatings(String title, Integer... ratings) {
        Optional<Movie> actual = moviesRepo.findMovieByTitle(title);
        if(actual.isPresent()) {
            ratingsRepo.insertRating(actual.get().getId(), Arrays.asList(ratings));
        } else {
            throw new IllegalArgumentException("Cannot find movie: " + title);
        }
    }

    public void addAverageRating(String title) {
        MovieWithRatings mwr = ratingsRepo.findRatingsByMovie(title);
        ratingsRepo.insertAverageRating(mwr);
    }
}
