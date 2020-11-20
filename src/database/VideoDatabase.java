package database;

import entertainment.Movie;
import entertainment.Show;

import java.util.List;

public class VideoDatabase {
    private List<Movie> movies;
    private List<Show> shows;

    public List<Movie> getMovies() {
        return movies;
    }

    public List<Show> getShows() {
        return shows;
    }

    @Override
    public String toString() {
        return "VideoDatabase{" +
                "movies=" + movies +
                ", shows=" + shows +
                '}';
    }

}
