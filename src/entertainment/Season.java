package entertainment;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a season of a tv show
 * <p>
 * DO NOT MODIFY
 */
public final class Season {
    /**
     * Number of current season
     */
    private final int currentSeason;
    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * List of ratings for each season
     */
    private List<Double> ratings;

    private ArrayList<String> userRated = new ArrayList<>();

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new ArrayList<>();
    }

    public double doSeasonRating() {
        double sum = 0;
        for (int i = 0; i < ratings.size(); i++) {
            sum += ratings.get(i);
        }
        if (ratings.size() != 0) {
            return sum / ratings.size();
        } else {
            return 0;
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(final List<Double> ratings) {
        this.ratings = ratings;
    }

    public ArrayList<String> getUserRated() {
        return userRated;
    }

    public void setUserRated(ArrayList<String> userRated) {
        this.userRated = userRated;
    }

    @Override
    public String toString() {
        return "Episode{"
                + "currentSeason="
                + currentSeason
                + ", duration="
                + duration
                + '}';
    }
}

