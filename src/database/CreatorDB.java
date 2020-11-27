package database;

import actor.Actor;
import entertainment.Movie;
import entertainment.Show;
import entities.User;
import fileio.ActorInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.List;

public final class CreatorDB {
    private final Input input;

    public CreatorDB(final Input input) {
        this.input = input;
    }

    /**
     * Generates an actor database
     * @return database object
     */
    public ActorDB generateActorDB() {
        List<ActorInputData> actorsInput = input.getActors();
        ArrayList<Actor> actors = new ArrayList<>();

        for (ActorInputData aux: actorsInput) {
            Actor actorAux = new Actor(aux.getName(), aux.getCareerDescription(),
                    aux.getFilmography(), aux.getAwards());
            actors.add(actorAux);
        }
        return new ActorDB(actors);
    }

    /**
     * Generates user database
     * @return a database object
     */
    public UserDB generateUserDB() {
        List<UserInputData> usersInput = input.getUsers();
        ArrayList<User> users = new ArrayList<>();

        for (UserInputData aux: usersInput) {
            User userAux = new User(aux.getUsername(), aux.getSubscriptionType(),
                    aux.getHistory(), aux.getFavoriteMovies());
            users.add(userAux);
        }
        return new UserDB(users);
     }

    /**
     * Generates a movie database
     * @return a database object
     */
    public MovieDB generateMovieDB() {
        List<MovieInputData> moviesInput = input.getMovies();
        ArrayList<Movie> movies = new ArrayList<>();

        for (MovieInputData aux: moviesInput) {
            Movie movieAux = new Movie(aux.getTitle(), aux.getYear(), aux.getCast(),
                    aux.getGenres(), aux.getDuration());
            movies.add(movieAux);
        }

        return new MovieDB(movies);
    }

    /**
     * Generates a show database
     * @return a database object
     */
    public ShowDB generateShowDB() {
        List<SerialInputData> showsInput = input.getSerials();
        ArrayList<Show> shows = new ArrayList<>();

        for (SerialInputData aux: showsInput) {
            Show showAux = new Show(aux.getTitle(), aux.getYear(), aux.getCast(), aux.getGenres(),
                    aux.getSeasons());
            shows.add(showAux);
        }
        return new ShowDB(shows);
    }
}
