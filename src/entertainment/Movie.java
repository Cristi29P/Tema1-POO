package entertainment;

import java.util.ArrayList;

public class Movie extends Video {
    private int movieLength;
    private ArrayList<Double> ratings = new ArrayList<>();
    private ArrayList<String> userRated = new ArrayList<>();


    public Movie() {

    }

    public Movie(String title, int launchYear, ArrayList<String> cast, ArrayList<String> genres, int movieLength) {
        super(title, launchYear, cast, genres);
        this.movieLength = movieLength;
    }

    public int getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    public ArrayList<Double> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<Double> ratings) {
        this.ratings = ratings;
    }

    public ArrayList<String> getUserRated() {
        return userRated;
    }

    public void setUserRated(ArrayList<String> userRated) {
        this.userRated = userRated;
    }
}
