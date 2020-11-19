package entertainment;

import java.util.ArrayList;

public class Show extends Video{
    private int numberOfSeasons;
    private ArrayList<Season> sezoane;

    public Show(String title, int launchYear, ArrayList<String> genres, int numberOfSeasons, ArrayList<Season> sezoane) {
        super(title, launchYear, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.sezoane = sezoane;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public ArrayList<Season> getSezoane() {
        return sezoane;
    }

    public void setSezoane(ArrayList<Season> sezoane) {
        this.sezoane = sezoane;
    }

    @Override
    public String toString() {
        return "Show{" +
                "numberOfSeasons=" + numberOfSeasons +
                ", sezoane=" + sezoane +
                '}';
    }
}
