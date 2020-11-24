package database;

import entertainment.Movie;

import java.util.ArrayList;

public final class MovieDB {
    private ArrayList<Movie> movies;

    public MovieDB(final ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(final ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
