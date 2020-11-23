package Actions;

import java.util.regex.*;
import actor.Actor;
import actor.ActorsAwards;
import database.ActorDatabase;
import database.MovieDatabase;
import database.ShowDatabase;
import database.UserDatabase;
import entertainment.Movie;
import entertainment.Show;
import entertainment.Video;
import entities.User;
import utils.Utils;

import java.util.*;

public class QueryExecutor {
    private String queryResult;

    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
    }

    public static void doActorAverage(Actor actor, MovieDatabase filme, ShowDatabase seriale) {
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

    public boolean hasAwards(Actor actor, List<String> awards) {
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

    public boolean hasWords(Actor actor, List<String> words) {
        boolean semafor = true;

        for (String aux: words) {
            Pattern pattern = Pattern.compile(".*\\b"+aux+"\\b.*");
            Matcher matcher = pattern.matcher(actor.getCareerDescription().toLowerCase());
            if (matcher.find()) {
                continue;
            } else {
                semafor = false;
                break;
            }
        }
        return semafor;
    }
    public int numberOfAwards(Actor actor, List<String> awards) {
        int contorAwards = 0;

//        for (String aux: awards) {
//            contorAwards += actor.getAwards().get(Utils.stringToAwards(aux));
//        }

        for (Map.Entry<ActorsAwards, Integer> entry: actor.getAwards().entrySet()) {
            contorAwards += entry.getValue();
        }

        return contorAwards;
    }

    public void getAverage(int number, String sortType, ActorDatabase actori, MovieDatabase filme, ShowDatabase seriale) {
        ArrayList<Actor> copieActori = new ArrayList<>(actori.getActors());

        for (Actor auxActor: copieActori) {
            doActorAverage(auxActor, filme, seriale);
        }

        copieActori.removeIf(actor -> actor.getAverageRating() == 0);



        Comparator<Actor> compareByRatings = Comparator.comparingDouble(Actor::getAverageRating);
        Comparator<Actor> compareByName = Comparator.comparing(Actor::getName);

        if (sortType.equals("asc")) {
            Collections.sort(copieActori, compareByName);
            Collections.sort(copieActori, compareByRatings);
        } else {
            Collections.sort(copieActori, compareByName.reversed());
            Collections.sort(copieActori, compareByRatings.reversed());
        }

        queryResult = "Query result: [";

        for(int i = 0; i < (Math.min(number, copieActori.size())); i++) {
            queryResult = queryResult + copieActori.get(i).getName()  + ", " ;

        }

        queryResult = removeLastChar(queryResult);
        queryResult = removeLastChar(queryResult);
        queryResult += "]";


    }

    public void getNumberOfRatings(int number, String sortType, UserDatabase users) {
        ArrayList<User> copieUsers = new ArrayList<>(users.getUsers());
        Comparator<User> compareByRatings = Comparator.comparingInt(User::getNumarDeRatinguriDate);
        Comparator<User> compareByName = Comparator.comparing(User::getUsername);

        copieUsers.removeIf(user -> user.getNumarDeRatinguriDate() == 0);

        if (sortType.equals("asc")) {
            Collections.sort(copieUsers, compareByName);
            Collections.sort(copieUsers, compareByRatings);
        } else {
            Collections.sort(copieUsers, compareByName.reversed());
            Collections.sort(copieUsers, compareByRatings.reversed());
        }

        queryResult = "Query result: [";

        for(int i = 0; i < (Math.min(number, copieUsers.size())); i++) {
            if (copieUsers.get(i).getNumarDeRatinguriDate() != 0) {
                queryResult = queryResult + copieUsers.get(i).getUsername()  + ", " ;
            }
        }

        queryResult = removeLastChar(queryResult);
        queryResult = removeLastChar(queryResult);
        queryResult += "]";
    }

    public void getByAwards(String sortType, ActorDatabase actori, List<List<String>> filtre) {
        ArrayList<Actor> copieActori = new ArrayList<>(actori.getActors());
        Comparator<Actor> compareByName = Comparator.comparing(Actor::getName);
        Comparator<Actor> compareByAwards = new Comparator<Actor>() {
            @Override
            public int compare(Actor o1, Actor o2) {
                return numberOfAwards(o1, filtre.get(3)) - numberOfAwards(o2, filtre.get(3));
            }
        };

        for (Iterator<Actor> it = copieActori.iterator(); it.hasNext(); ) {
            Actor aux = it.next();
            if (!hasAwards(aux, filtre.get(3))) {
                it.remove();
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(copieActori, compareByName);
            Collections.sort(copieActori, compareByAwards);
        } else {
            Collections.sort(copieActori, compareByName.reversed());
            Collections.sort(copieActori, compareByAwards.reversed());
        }

        if (copieActori.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for(int i = 0; i < copieActori.size(); i++) {
                queryResult = queryResult + copieActori.get(i).getName()  + ", " ;
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }

    public void getByDescription(String sortType, ActorDatabase actori, List<List<String>> filtre) {
        ArrayList<Actor> copieActori = new ArrayList<>(actori.getActors());
        Comparator<Actor> compareByName = Comparator.comparing(Actor::getName);

        for (Iterator<Actor> it = copieActori.iterator(); it.hasNext(); ) {
            Actor aux = it.next();
            if (!hasWords(aux, filtre.get(2))) {
                it.remove();
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(copieActori, compareByName);
        } else {
            Collections.sort(copieActori, compareByName.reversed());
        }



        if (copieActori.size() == 0) {
            queryResult = "Query result: []";
        } else {
            queryResult = "Query result: [";
            for(int i = 0; i < copieActori.size(); i++) {
                queryResult = queryResult + copieActori.get(i).getName()  + ", " ;
            }
            queryResult = removeLastChar(queryResult);
            queryResult = removeLastChar(queryResult);
            queryResult += "]";
        }
    }

    public void getRatedMovies(int number, String sortType, MovieDatabase filme, List<List<String>> filtre) {
        ArrayList<Movie> copieFilme = new ArrayList<>(filme.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByRating = Comparator.comparingDouble(Movie::doRating);

        // Scot filmele care au rating 0
        for (Iterator<Movie> it = copieFilme.iterator(); it.hasNext(); ) {
            Movie aux = it.next();
            if (aux.doRating() == 0) {
                it.remove();
            }
        }

        // Scot filmele care nu corespund anului
        for (Iterator<Movie> it = copieFilme.iterator(); it.hasNext(); ) {
            Movie aux = it.next();
            if (aux.getLaunchYear() != Integer.parseInt(filtre.get(0).get(0))) {
                it.remove();
            }
        }

        // Scot filmele care nu corespund genului
        for (Iterator<Movie> it = copieFilme.iterator(); it.hasNext(); ) {
            Movie aux = it.next();
            if (!aux.getGenres().contains(filtre.get(1).get(0))) {
                it.remove();
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(copieFilme, compareByTitle);
            Collections.sort(copieFilme, compareByRating);
        } else {
            Collections.sort(copieFilme, compareByTitle.reversed());
            Collections.sort(copieFilme, compareByRating.reversed());
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

    public void getRatedShows(int number, String sortType, ShowDatabase seriale, List<List<String>> filtre) {
        ArrayList<Show> copieSeriale = new ArrayList<>(seriale.getShows());
        Comparator<Show> compareByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> compareByRating = Comparator.comparingDouble(Show::doRating);

        for (Iterator<Show> it = copieSeriale.iterator(); it.hasNext(); ) {
            Show aux = it.next();
            if (aux.doRating() == 0) {
                it.remove();
            }
        }

        if (filtre.get(0).get(0) != null) {
            for (Iterator<Show> it = copieSeriale.iterator(); it.hasNext(); ) {
                Show aux = it.next();
                if (aux.getLaunchYear() != Integer.parseInt(filtre.get(0).get(0))) {
                    it.remove();
                }
            }
        }

        if (filtre.get(1).get(0) != null) {
            for (Iterator<Show> it = copieSeriale.iterator(); it.hasNext(); ) {
                Show aux = it.next();
                if (!aux.getGenres().contains(filtre.get(1).get(0))) {
                    it.remove();
                }
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(copieSeriale, compareByTitle);
            Collections.sort(copieSeriale, compareByRating);
        } else {
            Collections.sort(copieSeriale, compareByTitle.reversed());
            Collections.sort(copieSeriale, compareByRating.reversed());
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

    public void getLongestMovies(int number, String sortType, MovieDatabase filme, List<List<String>> filtre) {
        ArrayList<Movie> copieFilme = new ArrayList<>(filme.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByDuration = Comparator.comparingInt(Movie::getLength);

        if (filtre.get(0).get(0) != null) {
            for (Iterator<Movie> it = copieFilme.iterator(); it.hasNext(); ) {
                Movie aux = it.next();
                if (aux.getLaunchYear() != Integer.parseInt(filtre.get(0).get(0))) {
                    it.remove();
                }
            }
        }

        if (filtre.get(1).get(0) != null) {
            for (Iterator<Movie> it = copieFilme.iterator(); it.hasNext(); ) {
                Movie aux = it.next();
                if (!aux.getGenres().contains(filtre.get(1).get(0))) {
                    it.remove();
                }
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(copieFilme, compareByTitle);
            Collections.sort(copieFilme, compareByDuration);
        } else {
            Collections.sort(copieFilme, compareByTitle.reversed());
            Collections.sort(copieFilme, compareByDuration.reversed());
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

    public void getLongestShows(int number, String sortType, ShowDatabase seriale, List<List<String>> filtre) {
        ArrayList<Show> copieSeriale = new ArrayList<>(seriale.getShows());
        Comparator<Show> compareByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> compareByDuration = Comparator.comparingInt(Show::getLength);

        if (filtre.get(0).get(0) != null) {
            for (Iterator<Show> it = copieSeriale.iterator(); it.hasNext(); ) {
                Show aux = it.next();
                if (aux.getLaunchYear() != Integer.parseInt(filtre.get(0).get(0))) {
                    it.remove();
                }
            }
        }

        if (filtre.get(1).get(0) != null) {
            for (Iterator<Show> it = copieSeriale.iterator(); it.hasNext(); ) {
                Show aux = it.next();
                if (!aux.getGenres().contains(filtre.get(1).get(0))) {
                    it.remove();
                }
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(copieSeriale, compareByTitle);
            Collections.sort(copieSeriale, compareByDuration);
        } else {
            Collections.sort(copieSeriale, compareByTitle.reversed());
            Collections.sort(copieSeriale, compareByDuration.reversed());
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

    public void getFavoriteMovies(int number, String sortType, MovieDatabase filme, UserDatabase users,
                                  List<List<String>> filtre) {
        ArrayList<Movie> copieFilme = new ArrayList<>(filme.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByFavoriteNumber = Comparator.comparingInt(o -> o.numberOfFavorites(users));

        for (Iterator<Movie> it = copieFilme.iterator(); it.hasNext(); ) {
            Movie aux = it.next();
            if (aux.numberOfFavorites(users) == 0) {
                it.remove();
            }
        }
        if (filtre.get(0).get(0) != null) {
            for (Iterator<Movie> it = copieFilme.iterator(); it.hasNext(); ) {
                Movie aux = it.next();
                if (aux.getLaunchYear() != Integer.parseInt(filtre.get(0).get(0))) {
                    it.remove();
                }
            }
        }
        if (filtre.get(1).get(0) != null) {
            for (Iterator<Movie> it = copieFilme.iterator(); it.hasNext(); ) {
                Movie aux = it.next();
                if (!aux.getGenres().contains(filtre.get(1).get(0))) {
                    it.remove();
                }
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(copieFilme, compareByTitle);
            Collections.sort(copieFilme, compareByFavoriteNumber);
        } else {
            Collections.sort(copieFilme, compareByTitle.reversed());
            Collections.sort(copieFilme, compareByFavoriteNumber.reversed());
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

    public void getFavoriteShows(int number, String sortType, ShowDatabase seriale, UserDatabase users,
                                 List<List<String>> filtre) {
        ArrayList<Show>  copieSeriale = new ArrayList<>(seriale.getShows());
        Comparator<Show> compareByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> compareByFavoriteNumber = Comparator.comparingInt(o -> o.numberOfFavorites(users));

        for (Iterator<Show> it = copieSeriale.iterator(); it.hasNext(); ) {
            Show aux = it.next();
            if (aux.numberOfFavorites(users) == 0) {
                it.remove();
            }
        }
        if (filtre.get(0).get(0) != null) {
            for (Iterator<Show> it = copieSeriale.iterator(); it.hasNext(); ) {
                Show aux = it.next();
                if (aux.getLaunchYear() != Integer.parseInt(filtre.get(0).get(0))) {
                    it.remove();
                }
            }
        }
        if (filtre.get(1).get(0) != null) {
            for (Iterator<Show> it = copieSeriale.iterator(); it.hasNext(); ) {
                Show aux = it.next();
                if (!aux.getGenres().contains(filtre.get(1).get(0))) {
                    it.remove();
                }
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(copieSeriale, compareByTitle);
            Collections.sort(copieSeriale, compareByFavoriteNumber);
        } else {
            Collections.sort(copieSeriale, compareByTitle.reversed());
            Collections.sort(copieSeriale, compareByFavoriteNumber.reversed());
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

    public void getViewedMovies(int number, String sortType, MovieDatabase filme, UserDatabase users,
                                List<List<String>> filtre) {
        ArrayList<Movie> copieFilme = new ArrayList<>(filme.getMovies());
        Comparator<Movie> compareByTitle = Comparator.comparing(Movie::getTitle);
        Comparator<Movie> compareByViewNumber = Comparator.comparingInt(o -> o.numberOfViews(users));

        for (Iterator<Movie> it = copieFilme.iterator(); it.hasNext(); ) {
            Movie aux = it.next();
            if (aux.numberOfViews(users) == 0) {
                it.remove();
            }
        }
        if (filtre.get(0).get(0) != null) {
            for (Iterator<Movie> it = copieFilme.iterator(); it.hasNext(); ) {
                Movie aux = it.next();
                if (aux.getLaunchYear() != Integer.parseInt(filtre.get(0).get(0))) {
                    it.remove();
                }
            }
        }
        if (filtre.get(1).get(0) != null) {
            for (Iterator<Movie> it = copieFilme.iterator(); it.hasNext(); ) {
                Movie aux = it.next();
                if (!aux.getGenres().contains(filtre.get(1).get(0))) {
                    it.remove();
                }
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(copieFilme, compareByTitle);
            Collections.sort(copieFilme, compareByViewNumber);
        } else {
            Collections.sort(copieFilme, compareByTitle.reversed());
            Collections.sort(copieFilme, compareByViewNumber.reversed());
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

    public void getViewedShows(int number, String sortType, ShowDatabase seriale, UserDatabase users,
                               List<List<String>> filtre) {
        ArrayList<Show> copieSeriale = new ArrayList<>(seriale.getShows());
        Comparator<Show> compareByTitle = Comparator.comparing(Show::getTitle);
        Comparator<Show> compareByViewNumber = Comparator.comparingInt(o -> o.numberOfViews(users));

        for (Iterator<Show> it = copieSeriale.iterator(); it.hasNext(); ) {
            Show aux = it.next();
            if (aux.numberOfViews(users) == 0) {
                it.remove();
            }
        }
        if (filtre.get(0).get(0) != null) {
            for (Iterator<Show> it = copieSeriale.iterator(); it.hasNext(); ) {
                Show aux = it.next();
                if (aux.getLaunchYear() != Integer.parseInt(filtre.get(0).get(0))) {
                    it.remove();
                }
            }
        }
        if (filtre.get(1).get(0) != null) {
            for (Iterator<Show> it = copieSeriale.iterator(); it.hasNext(); ) {
                Show aux = it.next();
                if (!aux.getGenres().contains(filtre.get(1).get(0))) {
                    it.remove();
                }
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(copieSeriale, compareByTitle);
            Collections.sort(copieSeriale, compareByViewNumber);
        } else {
            Collections.sort(copieSeriale, compareByTitle.reversed());
            Collections.sort(copieSeriale, compareByViewNumber.reversed());
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


    public String getQueryResult() {
        return queryResult;
    }
}
