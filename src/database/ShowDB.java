package database;

import entertainment.Show;

import java.util.ArrayList;

public final class ShowDB {
    private ArrayList<Show> shows;

    public ShowDB(final ArrayList<Show> shows) {
        this.shows = shows;
    }

    public ArrayList<Show> getShows() {
        return shows;
    }

    public void setShows(final ArrayList<Show> shows) {
        this.shows = shows;
    }

}
