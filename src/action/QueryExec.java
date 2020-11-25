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
     * Removes the last character from a string
     * @param s the string provided
     * @return modified string
     */
    public static String removeLastChar(final String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
    }

    /**
     * Calculates the average of an actor based on the ratings of the video he/she appears in
     * @param actor caruia i se calculeaza average-ul
     * @param filme in which he/she played
     * @param seriale in which he/she played
     */
    public static void doActorAvg(final Actor actor, final MovieDB filme, final ShowDB seriale) {
        ArrayList<Video> videoclipuri = new ArrayList<>();
        videoclipuri.addAll(filme.getMovies());
        videoclipuri.addAll(seriale.getShows());

        double sum = 0;
        int contor = 0;

        for (String auxString: actor.getFilmography()) {
            for (Video auxVideo: videoclipuri) {
                if (auxVideo.getTitle().equals(auxString)) {
                    if (auxVideo.doRating() != 0) {
                        sum += auxVideo.doRating();
                        contor++;
                    }
                    break;
                }
            }
        }

        if (contor == 0) {
            actor.setAverageRating(0);
        } else {
            sum /= contor;
            actor.setAverageRating(sum);
        }

    }

    /**
     * Checks if an actor has all the awards specified
     * @param actor to be checked
     * @param awards needed
     * @return
     */
    public boolean hasAwards(final Actor actor, final List<String> awards) {
        boolean semafor = true;

        for (String aux: awards) {
            if (actor.getAwards().containsKey(Utils.stringToAwards(aux))) {
                continue;
            } else {
                semafor = false;
                break;
            }
        }
        return semafor;
    }

    /**
     * Checks if an actor's description contains all the provided words
     * @param actor to be checked
     * @param words list of words needed
     * @return
     */
    public boolean hasWords(final Actor actor, final List<String> words) {
        boolean semafor = true;

        for (String aux: words) {
            Pattern pattern = Pattern.compile(".*\\b" + aux + "\\b.*");
            Matcher matcher = pattern.matcher(actor.getDescription().toLowerCase());
            if (matcher.find()) {
                continue;
            } else {
                semafor = false;
                break;
            }
        }
        return semafor;
    }

    /**
     * Computes the number of awards of a specified actor
     * @param actor to be checked
     * @return number of awards
     */
    public int numberOfAwards(final Actor actor) {
        int contorAwards = 0;

        for (Map.Entry<ActorsAwards, Integer> entry: actor.getAwards().entrySet()) {
            contorAwards += entry.getValue();
        }

        return contorAwards;
    }

    /**
     * Main function to compute first actors based on their averages
     * @param number of actors returned
     * @param sortType asc/desc
     * @param actori database provided
     * @param filme database provided
     * @param seriale database provided
     */
    public void getAverage(final int number, final String sortType, final ActorDB actori,
                           final MovieDB filme, final ShowDB seriale) {
        ArrayList<Actor> copieActori = new ArrayList<>(actori.getActors());

        for (Actor auxActor: copieActori) {
            doActorAvg(auxActor, filme, seriale);
        }

        copieActori.removeIf(actor -> actor.getAverageRating() == 0);



        Comparator<Actor> compareByRatings = Comparator.comparingDouble(Actor::getAverageRating);
        Comparator<Actor> compareByName = Comparator.comparing(Actor::getName);

        if (sortType.equals("asc")) {
            copieActori.sort(compareByName);
            copieActori.sort(compareByRatings);
        } else {
            copieActori.sort(compareByName.reversed());
            copieActori.sort(compareByRatings.reversed());
        }

        queryResult = "Query result: [";

        for (int i = 0; i < (Math.min(number, copieActori.size())); i++) {
            queryResult = queryResult + copieActori.get(i).getName()  + ", ";

        }

        queryResult = removeLastChar(queryResult);
        queryResult = removeLastChar(queryResult);
        queryResult += "]";


    }

    /**
     * Computes the most active users based on their number of ratings
     * @param number of top users
     * @param sortType asc/desc
     * @param users database provided
     */
    public void getNumberOfRatings(final int number, final String sortType, final UserDB users) {
        ArrayList<User> copieUsers = new ArrayList<>(users.getUsers());
        Comparator<User> compareByRatings = Comparator.comparingInt(User::getNoRatings);
        Comparator<User> compareByName = Comparator.comparing(User::getUsername);

        copieUsers.removeIf(user -> user.getNoRatings() == 0);

        if (sortType.equals("asc")) {
            copieUsers.sort(compareByName);
            copieUsers.sort(compareByRatings);
        } else {
            copieUsers.sort(compareByName.reversed());
            copieUsers.sort(compareByRatings.reversed());
        }

        queryResult = "Query result: [";

        for (int i = 0; i < (Math.min(number, copieUsers.size())); i++) {
            if (copieUsers.get(i).getNoRatings() != 0) {
                queryResult = queryResult + copieUsers.get(i).getUsername()  + ", ";
            }
        }

        queryResult = removeLastChar(queryResult);
        queryResult = removeLastChar(queryResult);
        queryResult += "]";
    }

    /**
     * Computes the most awarded actors based on the number of awards received and their types
     * @param sortType asc/desc
     * @param actori database provided
     * @param filtre for awards provided
     */
    public void getByAwards(final String sortType, final ActorDB actori,
                            final List<List<String>> filtre) {
        ArrayList<Actor> copieActori = new ArrayList<>(actori.getActors());
        Comparator<Actor> compareByName = Comparator.comparing(Actor::getName);
        Comparator<Actor> compareByAwards = Comparator.comparingInt(this::numberOfAwards);

        copieActori.removeIf(aux -> !hasAwards(aux, filtre.get(filtre.size() - 1)));

        if (sortType.equals("asc")) {
            copieActori.sort(compareByName);
            copieActori.sort(compareByAwards);
        } else {
            copieActori.sort(compareByName.reversed());
            copieActori.sort(compareByAwards.reversed());
        }

        if (copieActori.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for (Actor actor : copieActori) {
                queryResult = queryResult + actor.getName() + ", ";
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }

    /**
     * Computes the actors that have a specific description
     * @param sortType asc/desc
     * @param actori database provided
     * @param filtre for words provided
     */
    public void getByDescription(final String sortType, final ActorDB actori,
                                 final List<List<String>> filtre) {
        ArrayList<Actor> copieActori = new ArrayList<>(actori.getActors());
        Comparator<Actor> compareByName = Comparator.comparing(Actor::getName);

        copieActori.removeIf(aux -> !hasWords(aux, filtre.get(2)));

        if (sortType.equals("asc")) {
            copieActori.sort(compareByName);
        } else {
            copieActori.sort(compareByName.reversed());
        }



        if (copieActori.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for (Actor actor : copieActori) {
                queryResult = queryResult + actor.getName() + ", ";
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }

    /**
     * Computes the most rated movies based on several criteria
     * @param number of top movies
     * @param sortType asc/desc
     * @param filme database provided
     * @param filtre for movies provided
     */
    public void getRatedMovies(final int number, final String sortType, final MovieDB filme,
                               final List<List<String>> filtre) {
        ArrayList<Movie> copieFilme = new ArrayList<>(filme.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByRating = Comparator.comparingDouble(Movie::doRating);

        // Scot filmele care au rating 0
        copieFilme.removeIf(aux -> aux.doRating() == 0);

        // Scot filmele care nu corespund anului
        copieFilme.removeIf(aux -> aux.getLaunchYear() != Integer.parseInt(filtre.get(0).get(0)));

        // Scot filmele care nu corespund genului
        copieFilme.removeIf(aux -> !aux.getGenres().contains(filtre.get(1).get(0)));

        if (sortType.equals("asc")) {
            copieFilme.sort(compareByTitle);
            copieFilme.sort(compareByRating);
        } else {
            copieFilme.sort(compareByTitle.reversed());
            copieFilme.sort(compareByRating.reversed());
        }


        if (copieFilme.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for (int i = 0; i < (Math.min(number, copieFilme.size())); i++) {
                queryResult = queryResult + copieFilme.get(i).getTitle() + ", ";
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }

    /**
     * Computes the most rated shows based on several criteria
     * @param number of top shows
     * @param sortType asc/desc
     * @param seriale database provided
     * @param filtre for shows provided
     */
    public void getRatedShows(final int number, final String sortType, final ShowDB seriale,
                              final List<List<String>> filtre) {
        ArrayList<Show> copieSeriale = new ArrayList<>(seriale.getShows());
        Comparator<Show> compareByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> compareByRating = Comparator.comparingDouble(Show::doRating);

        copieSeriale.removeIf(aux -> aux.doRating() == 0);

        if (filtre.get(0).get(0) != null) {
            copieSeriale.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filtre.get(0).get(0)));
        }

        if (filtre.get(1).get(0) != null) {
            copieSeriale.removeIf(aux -> !aux.getGenres().contains(filtre.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            copieSeriale.sort(compareByTitle);
            copieSeriale.sort(compareByRating);
        } else {
            copieSeriale.sort(compareByTitle.reversed());
            copieSeriale.sort(compareByRating.reversed());
        }

        if (copieSeriale.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for (int i = 0; i < (Math.min(number, copieSeriale.size())); i++) {
                queryResult = queryResult + copieSeriale.get(i).getTitle() + ", ";
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }

    /**
     * Computes the longest movies based on several criteria
     * @param number of top movies returned
     * @param sortType asc/desc
     * @param filme database
     * @param filtre for the movies provided
     */
    public void getLongestMovies(final int number, final String sortType, final MovieDB filme,
                                 final List<List<String>> filtre) {
        ArrayList<Movie> copieFilme = new ArrayList<>(filme.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByDuration = Comparator.comparingInt(Movie::getLength);

        if (filtre.get(0).get(0) != null) {
            copieFilme.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filtre.get(0).get(0)));
        }

        if (filtre.get(1).get(0) != null) {
            copieFilme.removeIf(aux -> !aux.getGenres().contains(filtre.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            copieFilme.sort(compareByTitle);
            copieFilme.sort(compareByDuration);
        } else {
            copieFilme.sort(compareByTitle.reversed());
            copieFilme.sort(compareByDuration.reversed());
        }

        if (copieFilme.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for (int i = 0; i < (Math.min(number, copieFilme.size())); i++) {
                queryResult = queryResult + copieFilme.get(i).getTitle() + ", ";
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }

    /**
     * Computes the longest shows based on several criteria
     * @param number of top shows returned
     * @param sortType asc/desc
     * @param seriale database provided
     * @param filtre for shows provided
     */
    public void getLongestShows(final int number, final String sortType, final ShowDB seriale,
                                final List<List<String>> filtre) {
        ArrayList<Show> copieSeriale = new ArrayList<>(seriale.getShows());
        Comparator<Show> compareByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> compareByDuration = Comparator.comparingInt(Show::getLength);

        if (filtre.get(0).get(0) != null) {
            copieSeriale.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filtre.get(0).get(0)));
        }

        if (filtre.get(1).get(0) != null) {
            copieSeriale.removeIf(aux -> !aux.getGenres().contains(filtre.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            copieSeriale.sort(compareByTitle);
            copieSeriale.sort(compareByDuration);
        } else {
            copieSeriale.sort(compareByTitle.reversed());
            copieSeriale.sort(compareByDuration.reversed());
        }

        if (copieSeriale.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for (int i = 0; i < (Math.min(number, copieSeriale.size())); i++) {
                queryResult = queryResult + copieSeriale.get(i).getTitle() + ", ";
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }

    /**
     * Computes favorite movies based on their number of appearances in user's favorite lists
     * @param number of favorite movies returned
     * @param sortType asc/desc
     * @param filme database provided
     * @param users database provided
     * @param filtre for movies provided
     */
    public void getFavoriteMovies(final int number, final String sortType, final MovieDB filme,
                                  final UserDB users, final List<List<String>> filtre) {
        ArrayList<Movie> copieFilme = new ArrayList<>(filme.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByFavNr = Comparator.comparingInt(o -> o.nrOfFavs(users));

        copieFilme.removeIf(aux -> aux.nrOfFavs(users) == 0);
        if (filtre.get(0).get(0) != null) {
            copieFilme.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filtre.get(0).get(0)));
        }
        if (filtre.get(1).get(0) != null) {
            copieFilme.removeIf(aux -> !aux.getGenres().contains(filtre.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            copieFilme.sort(compareByTitle);
            copieFilme.sort(compareByFavNr);
        } else {
            copieFilme.sort(compareByTitle.reversed());
            copieFilme.sort(compareByFavNr.reversed());
        }


        if (copieFilme.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for (int i = 0; i < (Math.min(number, copieFilme.size())); i++) {
                queryResult = queryResult + copieFilme.get(i).getTitle() + ", ";
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }

    /**
     * Computes favorite shows based on their number of appearances in user's favorite lists
     * @param number of top shows returned
     * @param sortType asc/desc
     * @param seriale database provided
     * @param users database provided
     * @param filtre for shows provided
     */
    public void getFavoriteShows(final int number, final String sortType, final ShowDB seriale,
                                 final UserDB users, final List<List<String>> filtre) {
        ArrayList<Show>  copieSeriale = new ArrayList<>(seriale.getShows());
        Comparator<Show> cmpByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> cmpByFavNumber = Comparator.comparingInt(o -> o.nrOfFavs(users));

        copieSeriale.removeIf(aux -> aux.nrOfFavs(users) == 0);
        if (filtre.get(0).get(0) != null) {
            copieSeriale.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filtre.get(0).get(0)));
        }
        if (filtre.get(1).get(0) != null) {
            copieSeriale.removeIf(aux -> !aux.getGenres().contains(filtre.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            copieSeriale.sort(cmpByTitle);
            copieSeriale.sort(cmpByFavNumber);
        } else {
            copieSeriale.sort(cmpByTitle.reversed());
            copieSeriale.sort(cmpByFavNumber.reversed());
        }

        if (copieSeriale.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for (int i = 0; i < (Math.min(number, copieSeriale.size())); i++) {
                queryResult = queryResult + copieSeriale.get(i).getTitle() + ", ";
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }

    /**
     * Computes the most viewed movies based on several criteria
     * @param number of top movies returned
     * @param sortType asc/desc
     * @param filme database returned
     * @param users database returned
     * @param filtre for movies provided
     */
    public void getViewedMovies(final int number, final String sortType, final MovieDB filme,
                                final UserDB users, final List<List<String>> filtre) {
        ArrayList<Movie> copieFilme = new ArrayList<>(filme.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByViewNumber = Comparator.comparingInt(o -> o.nrOfViews(users));

        copieFilme.removeIf(aux -> aux.nrOfViews(users) == 0);
        if (filtre.get(0).get(0) != null) {
            copieFilme.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filtre.get(0).get(0)));
        }
        if (filtre.get(1).get(0) != null) {
            copieFilme.removeIf(aux -> !aux.getGenres().contains(filtre.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            copieFilme.sort(compareByTitle);
            copieFilme.sort(compareByViewNumber);
        } else {
            copieFilme.sort(compareByTitle.reversed());
            copieFilme.sort(compareByViewNumber.reversed());
        }

        if (copieFilme.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for (int i = 0; i < (Math.min(number, copieFilme.size())); i++) {
                queryResult = queryResult + copieFilme.get(i).getTitle() + ", ";
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }

    /**
     * Computes the most viewed shows based on several criteria
     * @param number of top shows returned
     * @param sortType asc/desc
     * @param seriale database provided
     * @param users database provided
     * @param filtre for shows provided
     */
    public void getViewedShows(final int number, final String sortType, final ShowDB seriale,
                               final UserDB users, final List<List<String>> filtre) {
        ArrayList<Show> copieSeriale = new ArrayList<>(seriale.getShows());
        Comparator<Show> compareByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> compareByViewNumber = Comparator.comparingInt(o -> o.nrOfViews(users));

        copieSeriale.removeIf(aux -> aux.nrOfViews(users) == 0);
        if (filtre.get(0).get(0) != null) {
            copieSeriale.removeIf(aux -> aux.getLaunchYear()
                    != Integer.parseInt(filtre.get(0).get(0)));
        }
        if (filtre.get(1).get(0) != null) {
            copieSeriale.removeIf(aux -> !aux.getGenres().contains(filtre.get(1).get(0)));
        }

        if (sortType.equals("asc")) {
            copieSeriale.sort(compareByTitle);
            copieSeriale.sort(compareByViewNumber);
        } else {
            copieSeriale.sort(compareByTitle.reversed());
            copieSeriale.sort(compareByViewNumber.reversed());
        }

        if (copieSeriale.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for (int i = 0; i < (Math.min(number, copieSeriale.size())); i++) {
                queryResult = queryResult + copieSeriale.get(i).getTitle() + ", ";
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }


    public String getResult() {
        return queryResult;
    }
}
