package entertainment;

import database.UserDB;

import java.util.ArrayList;

public final class Show extends Video {
    private int numberOfSeasons;
    private ArrayList<Season> seasons;

    public Show(final String title, final int launchYear, final ArrayList<String> cast,
                final ArrayList<String> genres, final int numberOfSeasons,
                final ArrayList<Season> seasons) {
        super(title, launchYear, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    @Override
    public double doRating() {
        double sum = 0;
        for (Season season : seasons) {
            sum += season.doSeasonRating();
        }

        if (seasons.size() != 0) {
            return sum / seasons.size();
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

    /**
     * Returns the total number of views of the current show
     * @param users database provided
     * @return the number of views
     */
    public int nrOfViews(final UserDB users) {
        int counter = 0;
        for (int i = 0; i < users.getUsers().size(); i++) {
            if (users.getUsers().get(i).getHistory().containsKey(this.getTitle())) {
                counter += users.getUsers().get(i).getHistory().get(this.getTitle());
            }
        }
        return counter;
    }

    /**
     * Computes show total duration
     * @return total duration
     */
    public int getLength() {
        int sum = 0;
        for (Season season : seasons) {
            sum += season.getDuration();
        }
        return sum;
    }

    public void setNumberOfSeasons(final int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(final ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

}
