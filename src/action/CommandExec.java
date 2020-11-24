package action;

import database.MovieDB;
import database.ShowDB;
import entertainment.Movie;
import entertainment.Show;
import entities.User;

import java.util.ArrayList;

public final class CommandExec {
    private String commandResult;

    public void addFavorite(final String name, final String title, final ArrayList<User> users) {
        for (User user: users) {
            if (user.getUsername().equals(name)) {
                if (user.getHistory().containsKey(title) && user.getFavoriteMovies().contains(title)) {
                    commandResult = "error -> " + title + " is already in favourite list";
                } else if (user.getHistory().containsKey(title) && !(user.getFavoriteMovies().contains(title))) {
                    user.getFavoriteMovies().add(title);
                    commandResult = "success -> " + title + " was added as favourite";
                } else {
                    commandResult = "error -> " + title + " is not seen";
                }
            }
        }
    }

    public void addView(final String username, final String title, final ArrayList<User> users) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                if (user.getHistory().containsKey(title)) {
                    user.getHistory().replace(title, user.getHistory().get(title) + 1);
                } else {
                    user.getHistory().put(title, 1);
                }
                commandResult = "success -> " + title + " was viewed with total views of " + user.getHistory().get(title);
            }
        }
    }

    public boolean isMovie(final String title, final MovieDB filme) {
        for (Movie aux: filme.getMovies()) {
            if (aux.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }
    public Movie getMovieHook(final String title, final MovieDB filme) {
        for (Movie aux: filme.getMovies()) {
            if (aux.getTitle().equals(title)) {
                return aux;
            }
        }
        return null;
    }

    public Show getShowHook(final String title, final ShowDB seriale) {
        for (Show aux: seriale.getShows()) {
            if (aux.getTitle().equals(title)) {
                return aux;
            }
        }
        return null;
    }

    public void addRating(final String username, final String title, ArrayList<User> users,
                          final double grade, final int seasonNumber,
                          MovieDB filme, ShowDB seriale) {
        for (User user: users) {
            if (user.getUsername().equals(username)) { // CAUTAM USERUL
                if (user.getHistory().containsKey(title)) { // ESTE VAZUT CA SA PUTEM DA RATING
                    if (isMovie(title, filme)) { // ESTE FILM
                        Movie aux = getMovieHook(title, filme); // PRELUAM FILMUL
                        if (aux.getUserRated().contains(username)) { // A DAT RATING INAINTE
                            commandResult = "error -> " + title + " has been already rated";
                        } else { // NU A DAT RATING INAINTE
                            aux.getUserRated().add(username);
                            aux.getRatings().add(grade);
                            user.setNumarDeRatinguriDate(user.getNumarDeRatinguriDate() + 1);
                            commandResult = "success -> " + title + " was rated with " + grade +" by " + username;
                        }
                    } else { // ESTE SERIAL
                        Show aux = getShowHook(title, seriale);
                        if (aux.getSezoane().get(seasonNumber - 1).getUserRated().contains(username)) {
                            commandResult = "error -> " + title + " has been already rated";
                        } else {
                            aux.getSezoane().get(seasonNumber - 1).getUserRated().add(username);
                            aux.getSezoane().get(seasonNumber - 1).getRatings().add(grade);
                            user.setNumarDeRatinguriDate(user.getNumarDeRatinguriDate() + 1);
                            commandResult = "success -> " + title + " was rated with " + grade +" by " + username;
                        }
                    }
                } else { // NU A FOST VAZUT, NU PUTEM DA RATING
                    commandResult = "error -> " + title + " is not seen";
                }
            }
        }
    }

    public String getCommandResult() {
        return commandResult;
    }
}
