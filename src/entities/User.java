package entities;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private String username;
    private String subscriptionType;
    private Map<String, Integer> history;
    private ArrayList<String> favoriteMovies;
    private int numarDeRatinguriDate;

    public User() {

        numarDeRatinguriDate = 0;
    }

    public User(String username, String subscriptionType, Map<String, Integer> history, ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favoriteMovies = favoriteMovies;
        numarDeRatinguriDate = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public void setHistory(Map<String, Integer> history) {
        this.history = history;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void setFavoriteMovies(ArrayList<String> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }

    public int getNumarDeRatinguriDate() {
        return numarDeRatinguriDate;
    }

    public void setNumarDeRatinguriDate(int numarDeRatinguriDate) {
        this.numarDeRatinguriDate = numarDeRatinguriDate;
    }
}
