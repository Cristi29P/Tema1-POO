package entertainment;

import java.util.ArrayList;

public class Video {
    private String title;
    private int launchYear;
    private ArrayList<String> genres;

    public Video(String title, int launchYear, ArrayList<String> genres) {
        this.title = title;
        this.launchYear = launchYear;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLaunchYear() {
        return launchYear;
    }

    public void setLaunchYear(int launchYear) {
        this.launchYear = launchYear;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", launchYear=" + launchYear +
                ", genres=" + genres +
                '}';
    }
}
