package action;

import database.MovieDB;
import database.ShowDB;
import database.UserDB;
import entertainment.Video;
import entities.User;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import entertainment.TopGenres;


public final class RecommExec {
    private String recommendResult;

    /**
     * Returns the first unseen video of a user from the database
     * @param username of the user
     * @param filme database provided
     * @param seriale database provided
     * @param users database provided
     */
    public void stdRecomm(final String username, final MovieDB filme, final ShowDB seriale,
                          final UserDB users) {
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
            assert auxUser != null;
            if (!auxUser.getHistory().containsKey(aux.getTitle())) {
                recommendResult = "StandardRecommendation result: " + aux.getTitle();
                break;
            }
        }
    }

    /**
     * Returns the first unseen video with the highest rating for a user
     * @param username of the user
     * @param filme database provided
     * @param seriale database provided
     * @param users database provided
     */
    public void bestUnseenRecomm(final String username, final MovieDB filme, final ShowDB seriale,
                                 final UserDB users) {
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
            assert auxUser != null;
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

    /**
     * All the unseen videos from a certain genre and meet certain criteria
     * @param username of the user
     * @param genre of the videos
     * @param filme database provided
     * @param seriale database provided
     * @param users database provided
     */
    public void searchRecomm(final String username, final String genre, final MovieDB filme,
                             final ShowDB seriale, final UserDB users) {
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
        assert auxUser != null;
        if (auxUser.getSubscriptionType().equals("PREMIUM")) {
            for (Video aux: videoclipuri) {
                if ((!auxUser.getHistory().containsKey(aux.getTitle()))
                        && (aux.getGenres().contains(genre))) {
                    videos.add(aux);
                }
            }

            Comparator<Video> compareByTitle = Comparator.comparing(Video::getTitle);
            Comparator<Video> compareByRating = Comparator.comparingDouble(Video::doRating);

            videos.sort(compareByTitle);
            videos.sort(compareByRating);

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


    /**
     * Returns the total number of views of a video
     * @param video to check the number of views
     * @param users list provided
     * @return total number of views
     */
    public int numberOfViews(final Video video, final ArrayList<User> users) {
        int numberOfViews = 0;
            for (User user: users) {
                if (user.getHistory().containsKey(video.getTitle())) {
                    numberOfViews += user.getHistory().get(video.getTitle());
                }
            }
        return numberOfViews;
    }

    /**
     * Most popular unseen video from a certain genre
     * @param username of the user
     * @param filme database provided
     * @param seriale database provided
     * @param users database provided
     */
    public void popularRecomm(final String username, final MovieDB filme, final ShowDB seriale,
                              final UserDB users) {
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
                    popularitateGenuri.replace(gen, popularitateGenuri.get(gen)
                            + numberOfViews(video, users.getUsers()));
                }
            }
        }

        ArrayList<TopGenres> genuriPopulare = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : popularitateGenuri.entrySet()) {
            genuriPopulare.add(new TopGenres(entry.getKey(), entry.getValue()));
        }

        Comparator<TopGenres> cmpByPopularity = Comparator.comparingInt(TopGenres::getPopularity);
        genuriPopulare.sort(cmpByPopularity.reversed());

        assert auxUser != null;
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

            for (TopGenres gen: genuriPopulare) {
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

    /**
     * Returns the video that is the most common in the users' favorite lists
     * @param username of the user
     * @param filme database
     * @param seriale database
     * @param users database
     */
    public void favRecomm(final String username, final MovieDB filme, final ShowDB seriale,
                          final UserDB users) {
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

        assert auxUser != null;
        if (auxUser.getSubscriptionType().equals("PREMIUM")) {
            // Scoatem toate video-urile vazute
            for (Iterator<Video> it = videoclipuri.iterator(); it.hasNext();) {
                Video aux = it.next();
                if (auxUser.getHistory().containsKey(aux.getTitle())) {
                    it.remove();
                }
            }

            for (Video aux: videoclipuri) {
                if (aux.nrOfFavs(users) > favoriteNumber) {
                    favoriteNumber = aux.nrOfFavs(users);
                }
            }

            recommendResult = "FavoriteRecommendation cannot be applied!";
            for (Video aux: videoclipuri) {
                if (aux.nrOfFavs(users) == favoriteNumber) {
                    favoriteTitle = aux.getTitle();
                    recommendResult = "FavoriteRecommendation result: " + favoriteTitle;
                    break;
                }
            }
        } else {
            recommendResult = "FavoriteRecommendation cannot be applied!";
        }
    }

    public String getResult() {
        return recommendResult;
    }
}
