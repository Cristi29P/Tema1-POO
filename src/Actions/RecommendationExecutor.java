package Actions;

import database.MovieDatabase;
import database.ShowDatabase;
import database.UserDatabase;
import entertainment.Video;
import entities.User;

import java.util.*;
import java.util.stream.Collectors;
import entertainment.PopularGenres;


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

        for (Iterator<Video> it = videoclipuri.iterator(); it.hasNext();) {
            Video aux = it.next();
            if (auxUser.getHistory().containsKey(aux.getTitle())) {
                it.remove();
            }
        }

        for (Video aux: videoclipuri) {
            if (Double.compare(aux.doRating(), bestRating) == 1) {
                bestRating = aux.doRating();
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


    // Nr de vizualizari ale unui video
    public int numberOfViews(Video video, ArrayList<User> users) {
        int numberOfViews = 0;
            for (User user: users) {
                if (user.getHistory().containsKey(video.getTitle())) {
                    numberOfViews += user.getHistory().get(video.getTitle());
                }
            }
        return numberOfViews;
    }

    public void popularRecomm(String username, MovieDatabase filme, ShowDatabase seriale, UserDatabase users) {
        ArrayList<Video> videoclipuri = new ArrayList<>();
        videoclipuri.addAll(filme.getMovies());
        videoclipuri.addAll(seriale.getShows());
        User auxUser = null;

        for (User aux: users.getUsers()) {
            if (aux.getUsername().equals(username)) {
                auxUser = aux;
            }
        }

        Map<String, Integer> popularitateGenuri = new HashMap<>();

        // Obtinem harta de genuri vizionate
        for (Video video: videoclipuri) {
            for (String gen: video.getGenres()) {
                if (!popularitateGenuri.containsKey(gen)) {
                    popularitateGenuri.put(gen, numberOfViews(video, users.getUsers()));
                } else {
                    popularitateGenuri.replace(gen, popularitateGenuri.get(gen) + numberOfViews(video, users.getUsers()));
                }
            }
        }

        ArrayList<PopularGenres> genuriPopulare = new ArrayList<>();

        for(Map.Entry<String, Integer> entry : popularitateGenuri.entrySet()) {
            genuriPopulare.add(new PopularGenres(entry.getKey(), entry.getValue()));
        }

        Comparator<PopularGenres> compareByPopularity = Comparator.comparingInt(PopularGenres::getPopularity);
        Collections.sort(genuriPopulare, compareByPopularity.reversed());

        if (auxUser.getSubscriptionType().equals("PREMIUM")) {
            recommendResult = "PopularRecommendation cannot be applied!";

            // Scoatem toate video-urile vizionate
            for (Iterator<Video> it = videoclipuri.iterator(); it.hasNext();) {
                Video aux = it.next();
                if (auxUser.getHistory().containsKey(aux.getTitle())) {
                    it.remove();
                }
            }

            boolean semafor = false;

            for (PopularGenres gen: genuriPopulare) {
                for (Video video: videoclipuri) {
                    if (video.getGenres().contains(gen.getGenre())) {
                        recommendResult = "PopularRecommendation result: " + video.getTitle();
                        semafor = true;
                        break;
                    }
                }
                if (semafor) {
                    break;
                }
            }

        } else {
            recommendResult = "PopularRecommendation cannot be applied!";
        }
    }

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
