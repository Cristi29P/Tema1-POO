package Actions;

import actor.Actor;
import database.ActorDatabase;
import database.MovieDatabase;
import database.ShowDatabase;
import database.UserDatabase;
import entertainment.Video;
import entities.User;

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

        sum /= contor;
        actor.setAverageRating(sum);
    }

    public boolean hasAwards(Actor actor, List<String> awards) {
        boolean semafor = true;

        for (String aux: awards) {
            if (actor.getAwards().containsKey(awards)) {
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
            if (actor.getCareerDescription().toLowerCase().contains(aux.toLowerCase())) {
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

        for (String aux: awards) {
            contorAwards += actor.getAwards().get(awards);
        }

        return contorAwards;
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

    public String getQueryResult() {
        return queryResult;
    }
}
