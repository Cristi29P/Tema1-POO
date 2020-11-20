package database;

import entertainment.Movie;

import java.util.ArrayList;

public class MovieDatabase {
    ArrayList<Movie> movies;

    public MovieDatabase(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
