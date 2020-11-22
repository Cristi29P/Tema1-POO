package entertainment;

import database.MovieDatabase;
import database.UserDatabase;

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

    @Override
    public double doRating() {
        double sum = 0;

        for(int i = 0; i < ratings.size(); i++) {
            sum += ratings.get(i);
        }
        if (ratings.size() != 0) {
            return sum / ratings.size();
        } else {
            return 0;
        }
    }

    public int numberOfFavorites(UserDatabase users) {
        int contor = 0;
        for (int i = 0; i < users.getUsers().size(); i++) {
            if (users.getUsers().get(i).getFavoriteMovies().contains(this.getTitle())) {
                contor++;
            }
        }
        return contor;
    }

    public int getLength() {
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
