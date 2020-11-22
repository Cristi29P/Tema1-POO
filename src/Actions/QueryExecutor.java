package Actions;

import actor.Actor;
import database.ActorDatabase;
import database.MovieDatabase;
import database.ShowDatabase;
import database.UserDatabase;
import entertainment.Video;
import entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

        sum /= contor;
        actor.setAverageRating(sum);
    }

    public void getAverage(int number, String sortType, ActorDatabase actori, MovieDatabase filme, ShowDatabase seriale) {
        ArrayList<Actor> copieActori = new ArrayList<>(actori.getActors());

        for (Actor auxActor: copieActori) {
            doActorAverage(auxActor, filme, seriale);
        }

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


    public String getQueryResult() {
        return queryResult;
    }
}
