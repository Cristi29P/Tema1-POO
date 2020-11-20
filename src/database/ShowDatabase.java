package database;

import entertainment.Show;

import java.util.ArrayList;

public class ShowDatabase {
    ArrayList<Show> shows;

    public ShowDatabase(ArrayList<Show> shows) {
        this.shows = shows;
    }

    public ArrayList<Show> getShows() {
        return shows;
    }

    public void setShows(ArrayList<Show> shows) {
        this.shows = shows;
    }

}
