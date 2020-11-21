package Actions;

import actor.Actor;
import database.ActorDatabase;
import database.UserDatabase;
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

//    public void getAverage(int number, String sortType, ActorDatabase actori) {
//        ArrayList<Actor> copieActori = new ArrayList<>(actori.getActors());
//
//
//
//
//
//
//
//
//    }

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
