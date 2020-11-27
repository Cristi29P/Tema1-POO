package entertainment;

import database.UserDB;

import java.util.ArrayList;

public final class Movie extends Video {
    private int movieLength;
    private ArrayList<Double> ratings = new ArrayList<>();
    private final ArrayList<String> userRated = new ArrayList<>();


    public Movie() {

    }

    public Movie(final String title, final int launchYear, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int movieLength) {
        super(title, launchYear, cast, genres);
        this.movieLength = movieLength;
    }

    @Override
    public double doRating() {
        double sum = 0;

        for (Double rating : ratings) {
            sum += rating;
        }
        if (ratings.size() != 0) {
            return sum / ratings.size();
        } else {
            return 0;
        }
    }

    @Override
    public int nrOfFavorites(final UserDB users) {
        int counter = 0;
        for (int i = 0; i < users.getUsers().size(); i++) {
            if (users.getUsers().get(i).getFavMovies().contains(this.getTitle())) {
                counter++;
            }
        }
        return counter;
    }

    public int getLength() {
        return movieLength;
    }

    public ArrayList<Double> getRatings() {
        return ratings;
    }

    public void setRatings(final ArrayList<Double> ratings) {
        this.ratings = ratings;
    }

    public ArrayList<String> getUserRated() {
        return userRated;
    }
}
