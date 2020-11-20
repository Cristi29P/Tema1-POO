package entertainment;

import java.util.ArrayList;

public class Movie extends Video {
    private int movieLength;
    private ArrayList<Integer> ratings;

    public Movie() {

    }

    public Movie(String title, int launchYear, ArrayList<String> cast, ArrayList<String> genres, int movieLength, ArrayList<Integer> ratings) {
        super(title, launchYear, cast, genres);
        this.movieLength = movieLength;
        this.ratings = ratings;
    }

    public int getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    public ArrayList<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<Integer> ratings) {
        this.ratings = ratings;
    }

}
