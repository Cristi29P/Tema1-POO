package action;

import actor.Actor;
import actor.ActorsAwards;
import database.ActorDB;
import database.MovieDB;
import database.ShowDB;
import database.UserDB;
import entertainment.Movie;
import entertainment.Show;
import entertainment.Video;
import entities.User;
import utils.Utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QueryExec {
    private String queryResult;

    /**
     * Creates a list of strings with shows' titles
     * @param list of shows
     * @param number of shows to be added
     */
    public void createShowResult(final ArrayList<Show> list, final int number) {
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < (Math.min(number, list.size())); i++) {
            titles.add(list.get(i).getTitle());
        }
        queryResult = "Query result: " + titles.toString();
    }

    /**
     * Creates a list of strings with movies' titles
     * @param list of movies
     * @param number of movies to be added
     */
    public void createMovieResult(final ArrayList<Movie> list, final int number) {
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < (Math.min(number, list.size())); i++) {
            titles.add(list.get(i).getTitle());
        }
        queryResult = "Query result: " + titles.toString();
    }

    /**
     * Calculates the average of an actor based on the ratings of the video he/she appears in
     * @param actor of whom we calculate the average of
     * @param movies in which he/she played
     * @param shows in which he/she played
     */
    public static void doActorAvg(final Actor actor, final MovieDB movies, final ShowDB shows) {
        ArrayList<Video> videos = new ArrayList<>();
        videos.addAll(movies.getMovies());
        videos.addAll(shows.getShows());

        double sum = 0;
        int counter = 0;

        for (String auxString: actor.getFilmography()) {
            for (Video auxVideo: videos) {
                if (auxVideo.getTitle().equals(auxString)) {
                    if (auxVideo.doRating() != 0) {
                        sum += auxVideo.doRating();
                        counter++;
                    }
                    break;
                }
            }
        }

        if (counter == 0) {
            actor.setAverageRating(0);
        } else {
            sum /= counter;
            actor.setAverageRating(sum);
        }

    }

    /**
     * Checks if an actor has all the awards specified
     * @param actor to be checked
     * @param awards needed
     * @return true or false
     */
    public boolean hasAwards(final Actor actor, final List<String> awards) {
        boolean checker = true;

        for (String aux: awards) {
            if (!actor.getAwards().containsKey(Utils.stringToAwards(aux))) {
                checker = false;
                break;
            }
        }
        return checker;
    }

    /**
     * Checks if an actor's description contains all the provided words
     * @param actor to be checked
     * @param words list of words needed
     * @return true or false
     */
    public boolean hasWords(final Actor actor, final List<String> words) {
        boolean checker = true;

        for (String aux: words) {
            Pattern pattern = Pattern.compile(".*\\b" + aux + "\\b.*");
            Matcher matcher = pattern.matcher(actor.getDescription().toLowerCase());
            if (!matcher.find()) {
                checker = false;
                break;
            }
        }
        return checker;
    }

    /**
     * Computes the number of awards of a specified actor
     * @param actor to be checked
     * @return number of awards
     */
    public int numberOfAwards(final Actor actor) {
        int counterAwards = 0;

        for (Map.Entry<ActorsAwards, Integer> entry: actor.getAwards().entrySet()) {
            counterAwards += entry.getValue();
        }

        return counterAwards;
    }

    /**
     * Main function to compute first actors based on their averages
     * @param number of actors returned
     * @param sortType asc/desc
     * @param actors database provided
     * @param movies database provided
     * @param shows database provided
     */
    public void getAverage(final int number, final String sortType, final ActorDB actors,
                           final MovieDB movies, final ShowDB shows) {
        ArrayList<Actor> actorsCopy = new ArrayList<>(actors.getActors());

        for (Actor auxActor: actorsCopy) {
            doActorAvg(auxActor, movies, shows);
        }

        actorsCopy.removeIf(actor -> actor.getAverageRating() == 0);



        Comparator<Actor> compareByRatings = Comparator.comparingDouble(Actor::getAverageRating);
        Comparator<Actor> compareByName = Comparator.comparing(Actor::getName);

        if (sortType.equals("asc")) {
            actorsCopy.sort(compareByName);
            actorsCopy.sort(compareByRatings);
        } else {
            actorsCopy.sort(compareByName.reversed());
            actorsCopy.sort(compareByRatings.reversed());
        }

        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < (Math.min(number, actorsCopy.size())); i++) {
            names.add(actorsCopy.get(i).getName());
        }
        queryResult = "Query result: " + names.toString();
    }

    /**
     * Computes the most active users based on their number of ratings
     * @param number of top users
     * @param sortType asc/desc
     * @param users database provided
     */
    public void getNumberOfRatings(final int number, final String sortType, final UserDB users) {
        ArrayList<User> usersCopy = new ArrayList<>(users.getUsers());
        Comparator<User> compareByRatings = Comparator.comparingInt(User::getNoRatings);
        Comparator<User> compareByName = Comparator.comparing(User::getUsername);

        usersCopy.removeIf(user -> user.getNoRatings() == 0);

        if (sortType.equals("asc")) {
            usersCopy.sort(compareByName);
            usersCopy.sort(compareByRatings);
        } else {
            usersCopy.sort(compareByName.reversed());
            usersCopy.sort(compareByRatings.reversed());
        }

        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < (Math.min(number, usersCopy.size())); i++) {
            names.add(usersCopy.get(i).getUsername());
        }

        queryResult = "Query result: " + names.toString();
    }

    /**
     * Computes the most awarded actors based on the number of awards received and their types
     * @param sortType asc/desc
     * @param actors database provided
     * @param filters for awards provided
     */
    public void getByAwards(final String sortType, final ActorDB actors,
                            final List<List<String>> filters) {
        ArrayList<Actor> actorsCopy = new ArrayList<>(actors.getActors());
        Comparator<Actor> compareByName = Comparator.comparing(Actor::getName);
        Comparator<Actor> compareByAwards = Comparator.comparingInt(this::numberOfAwards);

        actorsCopy.removeIf(aux -> !hasAwards(aux, filters.get(filters.size() - 1)));

        if (sortType.equals("asc")) {
            actorsCopy.sort(compareByName);
            actorsCopy.sort(compareByAwards);
        } else {
            actorsCopy.sort(compareByName.reversed());
            actorsCopy.sort(compareByAwards.reversed());
        }

        ArrayList<String> names = new ArrayList<>();
        for (Actor actor: actorsCopy) {
            names.add(actor.getName());
        }
        queryResult = "Query result: " + names.toString();
    }

    /**
     * Computes the actors that have a specific description
     * @param sortType asc/desc
     * @param actors database provided
     * @param filters for words provided
     */
    public void getByDescription(final String sortType, final ActorDB actors,
                                 final List<List<String>> filters) {
        ArrayList<Actor> actorsCopy = new ArrayList<>(actors.getActors());
        Comparator<Actor> compareByName = Comparator.comparing(Actor::getName);

        actorsCopy.removeIf(aux -> !hasWords(aux, filters.get(2)));

        if (sortType.equals("asc")) {
            actorsCopy.sort(compareByName);
        } else {
            actorsCopy.sort(compareByName.reversed());
        }

        ArrayList<String> names = new ArrayList<>();
        for (Actor actor: actorsCopy) {
            names.add(actor.getName());
        }
        queryResult = "Query result: " + names.toString();
    }

    /**
     * Computes the most rated movies based on several criteria
     * @param number of top movies
     * @param sortType asc/desc
     * @param movies database provided
     * @param filters for movies provided
     */
    public void getRatedMovies(final int number, final String sortType, final MovieDB movies,
                               final List<List<String>> filters) {
        ArrayList<Movie> moviesCopy = new ArrayList<>(movies.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByRating = Comparator.comparingDouble(Movie::doRating);

        moviesCopy.removeIf(aux -> aux.doRating() == 0);
        moviesCopy.removeIf(aux -> aux.getLaunchYear() != Integer.parseInt(filters.get(0).get(0)));
        moviesCopy.removeIf(aux -> !aux.getGenres().contains(filters.get(1).get(0)));

        if (sortType.equals("asc")) {
            moviesCopy.sort(compareByTitle);
            moviesCopy.sort(compareByRating);
        } else {
            moviesCopy.sort(compareByTitle.reversed());
            moviesCopy.sort(compareByRating.reversed());
        }

        createMovieResult(moviesCopy, number);
    }

    /**
     * Computes the most rated shows based on several criteria
     * @param number of top shows
     * @param sortType asc/desc
     * @param shows database provided
     * @param filters for shows provided
     */
    public void getRatedShows(final int number, final String sortType, final ShowDB shows,
                              final List<List<String>> filters) {
        ArrayList<Show> showsCopy = new ArrayList<>(shows.getShows());
        Comparator<Show> compareByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> compareByRating = Comparator.comparingDouble(Show::doRating);

        showsCopy.removeIf(aux -> aux.doRating() == 0);

        if (filters.get(0).get(0) != null) {
            showsCopy.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filters.get(0).get(0)));
        }

        if (filters.get(1).get(0) != null) {
            showsCopy.removeIf(aux -> !aux.getGenres().contains(filters.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            showsCopy.sort(compareByTitle);
            showsCopy.sort(compareByRating);
        } else {
            showsCopy.sort(compareByTitle.reversed());
            showsCopy.sort(compareByRating.reversed());
        }

        createShowResult(showsCopy, number);
    }

    /**
     * Computes the longest movies based on several criteria
     * @param number of top movies returned
     * @param sortType asc/desc
     * @param movies database
     * @param filters for the movies provided
     */
    public void getLongestMovies(final int number, final String sortType, final MovieDB movies,
                                 final List<List<String>> filters) {
        ArrayList<Movie> moviesCopy = new ArrayList<>(movies.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByDuration = Comparator.comparingInt(Movie::getLength);

        if (filters.get(0).get(0) != null) {
            moviesCopy.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filters.get(0).get(0)));
        }

        if (filters.get(1).get(0) != null) {
            moviesCopy.removeIf(aux -> !aux.getGenres().contains(filters.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            moviesCopy.sort(compareByTitle);
            moviesCopy.sort(compareByDuration);
        } else {
            moviesCopy.sort(compareByTitle.reversed());
            moviesCopy.sort(compareByDuration.reversed());
        }

        createMovieResult(moviesCopy, number);
    }

    /**
     * Computes the longest shows based on several criteria
     * @param number of top shows returned
     * @param sortType asc/desc
     * @param shows database provided
     * @param filters for shows provided
     */
    public void getLongestShows(final int number, final String sortType, final ShowDB shows,
                                final List<List<String>> filters) {
        ArrayList<Show> showsCopy = new ArrayList<>(shows.getShows());
        Comparator<Show> compareByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> compareByDuration = Comparator.comparingInt(Show::getLength);

        if (filters.get(0).get(0) != null) {
            showsCopy.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filters.get(0).get(0)));
        }

        if (filters.get(1).get(0) != null) {
            showsCopy.removeIf(aux -> !aux.getGenres().contains(filters.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            showsCopy.sort(compareByTitle);
            showsCopy.sort(compareByDuration);
        } else {
            showsCopy.sort(compareByTitle.reversed());
            showsCopy.sort(compareByDuration.reversed());
        }

        createShowResult(showsCopy, number);
    }

    /**
     * Computes favorite movies based on their number of appearances in user's favorite lists
     * @param number of favorite movies returned
     * @param sortType asc/desc
     * @param movies database provided
     * @param users database provided
     * @param filters for movies provided
     */
    public void getFavoriteMovies(final int number, final String sortType, final MovieDB movies,
                                  final UserDB users, final List<List<String>> filters) {
        ArrayList<Movie> moviesCopy = new ArrayList<>(movies.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByFavNr = Comparator.comparingInt(o -> o.nrOfFavorites(users));

        moviesCopy.removeIf(aux -> aux.nrOfFavorites(users) == 0);
        if (filters.get(0).get(0) != null) {
            moviesCopy.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filters.get(0).get(0)));
        }
        if (filters.get(1).get(0) != null) {
            moviesCopy.removeIf(aux -> !aux.getGenres().contains(filters.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            moviesCopy.sort(compareByTitle);
            moviesCopy.sort(compareByFavNr);
        } else {
            moviesCopy.sort(compareByTitle.reversed());
            moviesCopy.sort(compareByFavNr.reversed());
        }


        createMovieResult(moviesCopy, number);
    }

    /**
     * Computes favorite shows based on their number of appearances in user's favorite lists
     * @param number of top shows returned
     * @param sortType asc/desc
     * @param shows database provided
     * @param users database provided
     * @param filters for shows provided
     */
    public void getFavoriteShows(final int number, final String sortType, final ShowDB shows,
                                 final UserDB users, final List<List<String>> filters) {
        ArrayList<Show>  showsCopy = new ArrayList<>(shows.getShows());
        Comparator<Show> cmpByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> cmpByFavNumber = Comparator.comparingInt(o -> o.nrOfFavorites(users));

        showsCopy.removeIf(aux -> aux.nrOfFavorites(users) == 0);
        if (filters.get(0).get(0) != null) {
            showsCopy.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filters.get(0).get(0)));
        }
        if (filters.get(1).get(0) != null) {
            showsCopy.removeIf(aux -> !aux.getGenres().contains(filters.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            showsCopy.sort(cmpByTitle);
            showsCopy.sort(cmpByFavNumber);
        } else {
            showsCopy.sort(cmpByTitle.reversed());
            showsCopy.sort(cmpByFavNumber.reversed());
        }

        createShowResult(showsCopy, number);
    }

    /**
     * Computes the most viewed movies based on several criteria
     * @param number of top movies returned
     * @param sortType asc/desc
     * @param movies database returned
     * @param users database returned
     * @param filters for movies provided
     */
    public void getViewedMovies(final int number, final String sortType, final MovieDB movies,
                                final UserDB users, final List<List<String>> filters) {
        ArrayList<Movie> moviesCopy = new ArrayList<>(movies.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByViewNumber = Comparator.comparingInt(o -> o.nrOfViews(users));

        moviesCopy.removeIf(aux -> aux.nrOfViews(users) == 0);
        if (filters.get(0).get(0) != null) {
            moviesCopy.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filters.get(0).get(0)));
        }
        if (filters.get(1).get(0) != null) {
            moviesCopy.removeIf(aux -> !aux.getGenres().contains(filters.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            moviesCopy.sort(compareByTitle);
            moviesCopy.sort(compareByViewNumber);
        } else {
            moviesCopy.sort(compareByTitle.reversed());
            moviesCopy.sort(compareByViewNumber.reversed());
        }

        createMovieResult(moviesCopy, number);
    }

    /**
     * Computes the most viewed shows based on several criteria
     * @param number of top shows returned
     * @param sortType asc/desc
     * @param shows database provided
     * @param users database provided
     * @param filters for shows provided
     */
    public void getViewedShows(final int number, final String sortType, final ShowDB shows,
                               final UserDB users, final List<List<String>> filters) {
        ArrayList<Show> showsCopy = new ArrayList<>(shows.getShows());
        Comparator<Show> compareByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> compareByViewNumber = Comparator.comparingInt(o -> o.nrOfViews(users));

        showsCopy.removeIf(aux -> aux.nrOfViews(users) == 0);
        if (filters.get(0).get(0) != null) {
            showsCopy.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filters.get(0).get(0)));
        }
        if (filters.get(1).get(0) != null) {
            showsCopy.removeIf(aux -> !aux.getGenres().contains(filters.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            showsCopy.sort(compareByTitle);
            showsCopy.sort(compareByViewNumber);
        } else {
            showsCopy.sort(compareByTitle.reversed());
            showsCopy.sort(compareByViewNumber.reversed());
        }

        createShowResult(showsCopy, number);
    }

    public String getResult() {
        return queryResult;
    }
}
