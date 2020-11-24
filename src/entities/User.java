package entities;

import java.util.ArrayList;
import java.util.Map;

public final class User {
    private String username;
    private String subscriptionType;
    private Map<String, Integer> history;
    private ArrayList<String> favMovies;
    private int noRatings;

    public User() {

        noRatings = 0;
    }

    public User(final String username, final String subscriptionType,
                final Map<String, Integer> history, final ArrayList<String> favMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favMovies = favMovies;
        noRatings = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(final String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public void setHistory(final Map<String, Integer> history) {
        this.history = history;
    }

    public ArrayList<String> getFavMovies() {
        return favMovies;
    }

    public void setFavMovies(final ArrayList<String> favMovies) {
        this.favMovies = favMovies;
    }

    public int getNoRatings() {
        return noRatings;
    }

    public void setNoRatings(final int noRatings) {
        this.noRatings = noRatings;
    }
}
