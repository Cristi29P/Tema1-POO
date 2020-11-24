package actor;

import java.util.ArrayList;
import java.util.Map;

public final class Actor {
    private String name;
    private String description;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;
    private double averageRating;

    public Actor() {

    }

    public Actor(final String name, final String description, final ArrayList<String> filmography,
                 final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.description = description;
        this.filmography = filmography;
        this.awards = awards;
        averageRating = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public void setAwards(final Map<ActorsAwards, Integer> awards) {
        this.awards = awards;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(final double averageRating) {
        this.averageRating = averageRating;
    }
}
