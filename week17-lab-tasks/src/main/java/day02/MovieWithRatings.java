package day02;

import java.util.List;

public class MovieWithRatings {

    private Long movieId;
    private List<Long> ratings;

    public MovieWithRatings(Long movieId, List<Long> ratings) {
        this.movieId = movieId;
        this.ratings = ratings;
    }

    public double countListAverage() {
        Long sumRate = ratings.stream().mapToLong(l -> l).sum();
        return (double) sumRate / ratings.size();
    }

    public Long getMovieId() {
        return movieId;
    }

    public List<Long> getRatings() {
        return ratings;
    }

    @Override
    public String toString() {
        return "MovieWithRatings{" +
                "movieId=" + movieId +
                ", ratings=" + ratings +
                '}';
    }
}
