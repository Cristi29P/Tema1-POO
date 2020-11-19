package entertainment;

import java.util.ArrayList;

public class Movie extends Video {
    private int movieLength;
    private int rating;

    public Movie(String title, int launchYear, ArrayList<String> genres, int movieLength, int rating) {
        super(title, launchYear, genres);
        this.movieLength = movieLength;
        this.rating = rating;
    }

    public int getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieLength=" + movieLength +
                ", rating=" + rating +
                '}';
    }
}
