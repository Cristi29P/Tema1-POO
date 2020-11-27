package entertainment;

import database.UserDB;

import java.util.ArrayList;

public class Video {
    private String title;
    private int launchYear;
    private ArrayList<String> cast;
    private ArrayList<String> genres;

    public Video() {

    }

    public Video(final String title, final int launchYear, final ArrayList<String> cast,
                 final ArrayList<String> genres) {
        this.title = title;
        this.launchYear = launchYear;
        this.cast = cast;
        this.genres = genres;
    }

    /**
     * Method inherited and overwritten in subclasses
     * @param users database provided
     * @return number of favorite apparitions
     */
    public int nrOfFavorites(final UserDB users) {
        return 0;
    }

    /**
     * Method inherited and overwritten in subclasses
     * @return rating of a video
     */
    public double doRating() {
        return 0;
    }

    /**
     * Returns video's title
     * @return video title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets a video title
     * @param title provided
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Returns video's launch year
     * @return launch year
     */
    public int getLaunchYear() {
        return launchYear;
    }

    /**
     * Sets a launch year for the video
     * @param launchYear provided
     */
    public void setLaunchYear(final int launchYear) {
        this.launchYear = launchYear;
    }

    /**
     * Returns a list containing the video cast
     * @return arraylist of strings
     */
    public ArrayList<String> getCast() {
        return cast;
    }

    /**
     * Sets the cast for a video
     * @param cast provided for the video
     */
    public void setCast(final ArrayList<String> cast) {
        this.cast = cast;
    }

    /**
     * Returns a list of genres for the video
     * @return an arraylist of strings
     */
    public ArrayList<String> getGenres() {
        return genres;
    }

    /**
     * Sets the genres for a video
     * @param genres  provided for the video
     */
    public void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

}
