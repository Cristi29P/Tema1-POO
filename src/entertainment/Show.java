package entertainment;

import database.UserDB;

import java.util.ArrayList;

public final class Show extends Video {
    private int numberOfSeasons;
    private ArrayList<Season> sezoane;

    public Show(final String title, final int launchYear, final ArrayList<String> cast,
                final ArrayList<String> genres, final int numberOfSeasons,
                final ArrayList<Season> sezoane) {
        super(title, launchYear, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.sezoane = sezoane;
    }

    @Override
    public double doRating() {
        double sum = 0;
        for (Season season : sezoane) {
            sum += season.doSeasonRating();
        }

        if (sezoane.size() != 0) {
            return sum / sezoane.size();
        } else {
            return 0;
        }
    }

    @Override
    public int nrOfFavs(final UserDB users) {
        int contor = 0;
        for (int i = 0; i < users.getUsers().size(); i++) {
            if (users.getUsers().get(i).getFavMovies().contains(this.getTitle())) {
                contor++;
            }
        }
        return contor;
    }

    /**
     * Returns the total number of views of the current show
     * @param users database provided
     * @return the number of views
     */
    public int nrOfViews(final UserDB users) {
        int contor = 0;
        for (int i = 0; i < users.getUsers().size(); i++) {
            if (users.getUsers().get(i).getHistory().containsKey(this.getTitle())) {
                contor += users.getUsers().get(i).getHistory().get(this.getTitle());
            }
        }
        return contor;
    }

    /**
     * Computes show total duration
     * @return total duration
     */
    public int getLength() {
        int sum = 0;
        for (Season season : sezoane) {
            sum += season.getDuration();
        }
        return sum;
    }

    public void setNumberOfSeasons(final int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public ArrayList<Season> getSezoane() {
        return sezoane;
    }

    public void setSezoane(final ArrayList<Season> sezoane) {
        this.sezoane = sezoane;
    }

}
