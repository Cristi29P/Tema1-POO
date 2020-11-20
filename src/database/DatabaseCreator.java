package database;

import actor.Actor;
import entertainment.Movie;
import entertainment.Show;
import entities.User;
import fileio.*;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class DatabaseCreator {
    private Input input;

    public DatabaseCreator(Input input) {
        this.input = input;
    }


    public ActorDatabase generateActorDatabase() {
        Actor actorAuxiliar = new Actor();

        List<ActorInputData> actorsInput = input.getActors();
        ArrayList<Actor> actors = new ArrayList<>();

        for(ActorInputData aux: actorsInput) {
            actorAuxiliar.setName(aux.getName());
            actorAuxiliar.setCareerDescription((aux.getCareerDescription()));
            actorAuxiliar.setFilmography(aux.getFilmography());
            actorAuxiliar.setAwards(aux.getAwards());
            actors.add(actorAuxiliar);
        }
        return new ActorDatabase(actors);
    }

    public UserDatabase generateUserDatabase() {
        User userAuxiliar = new User();

        List<UserInputData> usersInput = input.getUsers();
        ArrayList<User> users = new ArrayList<>();

        for(UserInputData aux: usersInput) {
            userAuxiliar.setUsername(aux.getUsername());
            userAuxiliar.setSubscriptionType(aux.getSubscriptionType());
            userAuxiliar.setHistory(aux.getHistory());
            userAuxiliar.setFavoriteMovies(aux.getFavoriteMovies());
            users.add(userAuxiliar);
        }

        return new UserDatabase(users);
     }

    public MovieDatabase generateMovieDatabase() {
        Movie movieAuxiliar = new Movie();

        List<MovieInputData> moviesInput = input.getMovies();
        ArrayList<Movie> movies = new ArrayList<>();

        for(MovieInputData aux: moviesInput) {
            movieAuxiliar.setMovieLength(aux.getDuration());
            movieAuxiliar.setCast(aux.getCast());
            movieAuxiliar.setGenres(aux.getGenres());
            movieAuxiliar.setTitle(aux.getTitle());
            movieAuxiliar.setLaunchYear(aux.getYear());
            movies.add(movieAuxiliar);
        }

        return new MovieDatabase(movies);
    }



    public ShowDatabase generateShowDatabase() {
        Show showAuxiliar = new Show();

        List<SerialInputData> showsInput = input.getSerials();
        ArrayList<Show> shows = new ArrayList<>();

        for(SerialInputData aux: showsInput) {
            showAuxiliar.setNumberOfSeasons(aux.getNumberSeason());
            showAuxiliar.setSezoane(aux.getSeasons());
            showAuxiliar.setCast(aux.getCast());
            showAuxiliar.setGenres(aux.getGenres());
            showAuxiliar.setLaunchYear(aux.getYear());
            showAuxiliar.setTitle(aux.getTitle());
            shows.add(showAuxiliar);
        }

        return new ShowDatabase(shows);
    }

}
