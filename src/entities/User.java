package entities;

import java.util.ArrayList;
import java.util.Map;

public final class User {
    private String username;
    private String subscriptionType;
    private Map<String, Integer> history;
    private ArrayList<String> favoriteMovies;
    private int numarDeRatinguriDate;

    public User() {

        numarDeRatinguriDate = 0;
    }

    public User(final String username, final String subscriptionType,
                final Map<String, Integer> history, final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favoriteMovies = favoriteMovies;
        numarDeRatinguriDate = 0;
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

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void setFavoriteMovies(final ArrayList<String> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }

    public int getNumarDeRatinguriDate() {
        return numarDeRatinguriDate;
    }

    public void setNumarDeRatinguriDate(int numarDeRatinguriDate) {
        this.numarDeRatinguriDate = numarDeRatinguriDate;
    }
}
