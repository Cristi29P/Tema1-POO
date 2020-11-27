package action;

import database.MovieDB;
import database.ShowDB;
import entertainment.Movie;
import entertainment.Show;
import entities.User;

import java.util.ArrayList;

public final class CommandExec {
    private String commandResult;

    /**
     * Adds a video to a user's favorite list
     * @param name of the user provided
     * @param title of the movie provided
     * @param users database provided
     */
    public void addFavorites(final String name, final String title, final ArrayList<User> users) {
        for (User user: users) {
            if (user.getUsername().equals(name)) {
                if (user.getHistory().containsKey(title) && user.getFavMovies().contains(title)) {
                    commandResult = "error -> " + title + " is already in favourite list";
                } else if (user.getHistory().containsKey(title)
                        && !(user.getFavMovies().contains(title))) {
                    user.getFavMovies().add(title);
                    commandResult = "success -> " + title + " was added as favourite";
                } else {
                    commandResult = "error -> " + title + " is not seen";
                }
            }
        }
    }

    /**
     * Increments the numbers of views of a video in a user's history
     * @param username of the user provided
     * @param title of the video provided
     * @param users database provided
     */
    public void addView(final String username, final String title, final ArrayList<User> users) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                if (user.getHistory().containsKey(title)) {
                    user.getHistory().replace(title, user.getHistory().get(title) + 1);
                } else {
                    user.getHistory().put(title, 1);
                }
                commandResult = "success -> " + title + " was viewed with total views of "
                        + user.getHistory().get(title);
            }
        }
    }

    /**
     * Checks if a movie exists in the database
     * @param title of the movie provided
     * @param movies database provided
     * @return true or false if the movie exists or not
     */
    public boolean isMovie(final String title, final MovieDB movies) {
        for (Movie aux: movies.getMovies()) {
            if (aux.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a reference to a specific movie by its title
     * @param title of the movie provided
     * @param movies database provided
     * @return reference to a movie object
     */
    public Movie getMovieHook(final String title, final MovieDB movies) {
        for (Movie aux: movies.getMovies()) {
            if (aux.getTitle().equals(title)) {
                return aux;
            }
        }
        return null;
    }

    /**
     * Returns a reference to a show by its title
     * @param title of the show provided
     * @param shows database provided
     * @return a reference to a show object
     */
    public Show getShowHook(final String title, final ShowDB shows) {
        for (Show aux: shows.getShows()) {
            if (aux.getTitle().equals(title)) {
                return aux;
            }
        }
        return null;
    }

    /**
     * Rates a movie or a show
     * @param username of the user providing the rating
     * @param title of the video that needs to be rated
     * @param users database
     * @param grade of the video
     * @param seasonNr of a show
     * @param movies database provided
     * @param shows database provided
     */
    public void addRating(final String username, final String title, final ArrayList<User> users,
                          final double grade, final int seasonNr,
                          final MovieDB movies, final ShowDB shows) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                if (user.getHistory().containsKey(title)) {
                    if (isMovie(title, movies)) {
                        Movie aux = getMovieHook(title, movies);
                        assert aux != null;
                        if (aux.getUserRated().contains(username)) {
                            commandResult = "error -> " + title + " has been already rated";
                        } else {
                            aux.getUserRated().add(username);
                            aux.getRatings().add(grade);
                            user.setNoRatings(user.getNoRatings() + 1);
                            commandResult = "success -> " + title + " was rated with "
                                    + grade + " by " + username;
                        }
                    } else {
                        Show aux = getShowHook(title, shows);
                        assert aux != null;
                        if (aux.getSeasons().get(seasonNr - 1).getUserRated().contains(username)) {
                            commandResult = "error -> " + title + " has been already rated";
                        } else {
                            aux.getSeasons().get(seasonNr - 1).getUserRated().add(username);
                            aux.getSeasons().get(seasonNr - 1).getRatings().add(grade);
                            user.setNoRatings(user.getNoRatings() + 1);
                            commandResult = "success -> " + title + " was rated with "
                                    + grade + " by " + username;
                        }
                    }
                } else {
                    commandResult = "error -> " + title + " is not seen";
                }
            }
        }
    }

    public String getResult() {
        return commandResult;
    }
}
