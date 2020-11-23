package entertainment;

import database.UserDatabase;

import java.util.ArrayList;

public class Show extends Video{
    private int numberOfSeasons;
    private ArrayList<Season> sezoane;

    public Show() {

    }

    public Show(String title, int launchYear, ArrayList<String> cast, ArrayList<String> genres, int numberOfSeasons, ArrayList<Season> sezoane) {
        super(title, launchYear, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.sezoane = sezoane;
    }

    @Override
    public double doRating() {
        double sum = 0;
        for (int i = 0; i < sezoane.size(); i++) {
            sum += sezoane.get(i).doSeasonRating();
        }

        if (sezoane.size() != 0) {
            return sum / sezoane.size();
        } else {
            return 0;
        }
    }

    @Override
    public int numberOfFavorites(UserDatabase users) {
        int contor = 0;
        for (int i = 0; i < users.getUsers().size(); i++) {
            if (users.getUsers().get(i).getFavoriteMovies().contains(this.getTitle())) {
                contor++;
            }
        }
        return contor;
    }

    public int numberOfViews(UserDatabase users) {
        int contor = 0;
        for (int i = 0; i < users.getUsers().size(); i++) {
            if (users.getUsers().get(i).getHistory().containsKey(this.getTitle())) {
                contor += users.getUsers().get(i).getHistory().get(this.getTitle());
            }
        }
        return contor;
    }

    public int getLength() {
        int sum = 0;
        for (int i = 0; i < sezoane.size(); i++) {
            sum += sezoane.get(i).getDuration();
        }
        return sum;
    }

    public int getNumberOfSeasons() {return numberOfSeasons; }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public ArrayList<Season> getSezoane() {
        return sezoane;
    }

    public void setSezoane(ArrayList<Season> sezoane) {
        this.sezoane = sezoane;
    }

}
