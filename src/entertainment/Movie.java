package entertainment;

import database.UserDB;

import java.util.ArrayList;

public final class Movie extends Video {
    private int movieLength;
    private ArrayList<Double> ratings = new ArrayList<>();
    private ArrayList<String> userRated = new ArrayList<>();


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

        for(int i = 0; i < ratings.size(); i++) {
            sum += ratings.get(i);
        }
        if (ratings.size() != 0) {
            return sum / ratings.size();
        } else {
            return 0;
        }
    }

    @Override
    public int numberOfFavorites(final UserDB users) {
        int contor = 0;
        for (int i = 0; i < users.getUsers().size(); i++) {
            if (users.getUsers().get(i).getFavoriteMovies().contains(this.getTitle())) {
                contor++;
            }
        }
        return contor;
    }

    public int numberOfViews(final UserDB users) {
        int contor = 0;
        for (int i = 0; i < users.getUsers().size(); i++) {
            if (users.getUsers().get(i).getHistory().containsKey(this.getTitle())) {
                contor += users.getUsers().get(i).getHistory().get(this.getTitle());
            }
        }
        return contor;
    }

    public int getLength() {
        return movieLength;
    }

    public void setMovieLength(final int movieLength) {
        this.movieLength = movieLength;
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
