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


    public ActorDB generateActorDB() {


        List<ActorInputData> actorsInput = input.getActors();
        ArrayList<Actor> actors = new ArrayList<>();

        for (ActorInputData aux: actorsInput) {
            Actor actorAuxiliar = new Actor();
            actorAuxiliar.setName(aux.getName());
            actorAuxiliar.setDescription((aux.getCareerDescription()));
            actorAuxiliar.setFilmography(aux.getFilmography());
            actorAuxiliar.setAwards(aux.getAwards());
            actors.add(actorAuxiliar);
        }
        return new ActorDB(actors);
    }

    public UserDB generateUserDB() {


        List<UserInputData> usersInput = input.getUsers();
        ArrayList<User> users = new ArrayList<>();

        for (UserInputData aux: usersInput) {
            User userAuxiliar = new User();
            userAuxiliar.setUsername(aux.getUsername());
            userAuxiliar.setSubscriptionType(aux.getSubscriptionType());
            userAuxiliar.setHistory(aux.getHistory());
            userAuxiliar.setFavMovies(aux.getFavoriteMovies());
            users.add(userAuxiliar);
        }

        return new UserDB(users);
     }

    public MovieDB generateMovieDB() {


        List<MovieInputData> moviesInput = input.getMovies();
        ArrayList<Movie> movies = new ArrayList<>();

        for (MovieInputData aux: moviesInput) {
            Movie movieAuxiliar = new Movie();
            movieAuxiliar.setMovieLength(aux.getDuration());
            movieAuxiliar.setCast(aux.getCast());
            movieAuxiliar.setGenres(aux.getGenres());
            movieAuxiliar.setTitle(aux.getTitle());
            movieAuxiliar.setLaunchYear(aux.getYear());
            movies.add(movieAuxiliar);
        }

        return new MovieDB(movies);
    }



    public ShowDB generateShowDB() {


        List<SerialInputData> showsInput = input.getSerials();
        ArrayList<Show> shows = new ArrayList<>();

        for (SerialInputData aux: showsInput) {
            Show showAuxiliar = new Show();
            showAuxiliar.setNumberOfSeasons(aux.getNumberSeason());
            showAuxiliar.setSezoane(aux.getSeasons());
            showAuxiliar.setCast(aux.getCast());
            showAuxiliar.setGenres(aux.getGenres());
            showAuxiliar.setLaunchYear(aux.getYear());
            showAuxiliar.setTitle(aux.getTitle());
            shows.add(showAuxiliar);
        }

        return new ShowDB(shows);
    }
}
