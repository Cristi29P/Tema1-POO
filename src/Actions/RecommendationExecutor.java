package Actions;

import database.MovieDatabase;
import database.ShowDatabase;
import database.UserDatabase;
import entertainment.Video;
import entities.User;

import java.util.ArrayList;

public class RecommendationExecutor {
    private String recommendResult;

    public void stdRecomm(String username, MovieDatabase filme, ShowDatabase seriale, UserDatabase users) {
        ArrayList<Video> videoclipuri = new ArrayList<>();
        videoclipuri.addAll(filme.getMovies());
        videoclipuri.addAll(seriale.getShows());
        User auxUser = null;

        for (User aux: users.getUsers()) {
            if (aux.getUsername().equals(username)) {
                auxUser = aux;
            }
        }
        recommendResult = "StandardRecommendation cannot be applied!";
        for (Video aux: videoclipuri) {
            if (!auxUser.getHistory().containsKey(aux.getTitle())) {
                recommendResult = "StandardRecommendation result: " + aux.getTitle();
                break;
            }
        }
    }

    public void bestUnseenRecomm(String username, MovieDatabase filme, ShowDatabase seriale, UserDatabase users) {
        ArrayList<Video> videoclipuri = new ArrayList<>();
        videoclipuri.addAll(filme.getMovies());
        videoclipuri.addAll(seriale.getShows());
        User auxUser = null;
        String bestTitle;
        double bestRating = -1;

        for (User aux: users.getUsers()) {
            if (aux.getUsername().equals(username)) {
                auxUser = aux;
            }
        }

        for (Video aux: videoclipuri) {
            if (!auxUser.getHistory().containsKey(aux.getTitle())) {
                if (aux.doRating() > bestRating) {
                    bestRating = aux.doRating();
                }
            }
        }

        recommendResult = "BestRatedUnseenRecommendation cannot be applied!";
        for (Video aux: videoclipuri) {
            if (aux.doRating() == bestRating) {
                bestTitle = aux.getTitle();
                recommendResult = "BestRatedUnseenRecommendation result: " + bestTitle;
                break;
            }
        }

    }

    public String getRecommendResult() {
        return recommendResult;
    }
}
