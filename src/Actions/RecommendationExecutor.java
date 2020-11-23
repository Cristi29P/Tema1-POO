package Actions;

import database.MovieDatabase;
import database.ShowDatabase;
import database.UserDatabase;
import entertainment.Movie;
import entertainment.Video;
import entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

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

    public void searchRecomm(String username, String genre, MovieDatabase filme, ShowDatabase seriale,
                             UserDatabase users) {
        ArrayList<Video> videoclipuri = new ArrayList<>();
        ArrayList<Video> videos = new ArrayList<>();
        videoclipuri.addAll(filme.getMovies());
        videoclipuri.addAll(seriale.getShows());
        User auxUser = null;

        for (User aux: users.getUsers()) {
            if (aux.getUsername().equals(username)) {
                auxUser = aux;
            }
        }

        // Check if user has premium subscription
        if (auxUser.getSubscriptionType().equals("PREMIUM")) {
            for (Video aux: videoclipuri) {
                if ((!auxUser.getHistory().containsKey(aux.getTitle())) && (aux.getGenres().contains(genre))) {
                    videos.add(aux);
                }
            }

            Comparator<Video> compareByTitle = Comparator.comparing(Video::getTitle);
            Comparator<Video> compareByRating = Comparator.comparingDouble(Video::doRating);

            Collections.sort(videos, compareByTitle);
            Collections.sort(videos, compareByRating);

            ArrayList<String> videoTitles = new ArrayList<>();

            for (Video aux: videos) {
                videoTitles.add(aux.getTitle());
            }
            if (videoTitles.size() != 0) {
                recommendResult = "SearchRecommendation result: " + videoTitles.toString();
            } else {
                recommendResult = "SearchRecommendation cannot be applied!";
            }

        } else {
            recommendResult = "SearchRecommendation cannot be applied!";
        }
    }

//    public void popularRecomm() {
//
//    }

    public void favoriteRecomm(String username,  MovieDatabase filme, ShowDatabase seriale, UserDatabase users) {
        ArrayList<Video> videoclipuri = new ArrayList<>();
        videoclipuri.addAll(filme.getMovies());
        videoclipuri.addAll(seriale.getShows());
        User auxUser = null;
        int favoriteNumber = -1;
        String favoriteTitle;

        for (User aux: users.getUsers()) {
            if (aux.getUsername().equals(username)) {
                auxUser = aux;
            }
        }

        if (auxUser.getSubscriptionType().equals("PREMIUM")) {
            // Scoatem toate video-urile vazute
            for (Iterator<Video> it = videoclipuri.iterator(); it.hasNext();) {
                Video aux = it.next();
                if (auxUser.getHistory().containsKey(aux.getTitle())) {
                    it.remove();
                }
            }

            for (Video aux: videoclipuri) {
                if (aux.numberOfFavorites(users) > favoriteNumber) {
                    favoriteNumber = aux.numberOfFavorites(users);
                }
            }

            recommendResult = "FavoriteRecommendation cannot be applied!";
            for (Video aux: videoclipuri) {
                if (aux.numberOfFavorites(users) == favoriteNumber) {
                    favoriteTitle = aux.getTitle();
                    recommendResult = "FavoriteRecommendation result: " + favoriteTitle;
                    break;
                }
            }
        } else {
            recommendResult = "FavoriteRecommendation cannot be applied!";
        }
    }

    public String getRecommendResult() {
        return recommendResult;
    }
}
