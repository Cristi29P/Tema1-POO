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


public final class RecExec {
    private String recommendResult;

    /**
     * Returns the first unseen video of a user from the database
     * @param username of the user
     * @param movies database provided
     * @param shows database provided
     * @param users database provided
     */
    public void stdRec(final String username, final MovieDB movies, final ShowDB shows,
                       final UserDB users) {
        ArrayList<Video> videos = new ArrayList<>();
        videos.addAll(movies.getMovies());
        videos.addAll(shows.getShows());
        User auxUser = null;

        for (User aux: users.getUsers()) {
            if (aux.getUsername().equals(username)) {
                auxUser = aux;
            }
        }
        recommendResult = "StandardRecommendation cannot be applied!";
        for (Video aux: videos) {
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
     * @param movies database provided
     * @param shows database provided
     * @param users database provided
     */
    public void bestUnseenRec(final String username, final MovieDB movies, final ShowDB shows,
                              final UserDB users) {
        ArrayList<Video> videos = new ArrayList<>();
        videos.addAll(movies.getMovies());
        videos.addAll(shows.getShows());
        User auxUser = null;
        String bestTitle;
        double bestRating = -1;

        for (User aux: users.getUsers()) {
            if (aux.getUsername().equals(username)) {
                auxUser = aux;
            }
        }

        for (Iterator<Video> it = videos.iterator(); it.hasNext();) {
            Video aux = it.next();
            assert auxUser != null;
            if (auxUser.getHistory().containsKey(aux.getTitle())) {
                it.remove();
            }
        }

        for (Video aux: videos) {
            if (Double.compare(aux.doRating(), bestRating) == 1) {
                bestRating = aux.doRating();
            }
        }

        recommendResult = "BestRatedUnseenRecommendation cannot be applied!";
        for (Video aux: videos) {
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
     * @param movies database provided
     * @param shows database provided
     * @param users database provided
     */
    public void searchRec(final String username, final String genre, final MovieDB movies,
                          final ShowDB shows, final UserDB users) {
        ArrayList<Video> videos = new ArrayList<>();
        ArrayList<Video> videosSort = new ArrayList<>();
        videos.addAll(movies.getMovies());
        videos.addAll(shows.getShows());
        User auxUser = null;

        for (User aux: users.getUsers()) {
            if (aux.getUsername().equals(username)) {
                auxUser = aux;
            }
        }

        // Check if user has premium subscription
        assert auxUser != null;
        if (auxUser.getSubscriptionType().equals("PREMIUM")) {
            for (Video aux: videos) {
                if ((!auxUser.getHistory().containsKey(aux.getTitle()))
                        && (aux.getGenres().contains(genre))) {
                    videosSort.add(aux);
                }
            }

            Comparator<Video> compareByTitle = Comparator.comparing(Video::getTitle);
            Comparator<Video> compareByRating = Comparator.comparingDouble(Video::doRating);

            videosSort.sort(compareByTitle);
            videosSort.sort(compareByRating);

            ArrayList<String> videoTitles = new ArrayList<>();

            for (Video aux: videosSort) {
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
     * @param movies database provided
     * @param shows database provided
     * @param users database provided
     */
    public void popularRec(final String username, final MovieDB movies, final ShowDB shows,
                           final UserDB users) {
        ArrayList<Video> videos = new ArrayList<>();
        videos.addAll(movies.getMovies());
        videos.addAll(shows.getShows());
        User auxUser = null;

        for (User aux: users.getUsers()) {
            if (aux.getUsername().equals(username)) {
                auxUser = aux;
            }
        }

        Map<String, Integer> popularGenresMap = new HashMap<>();

        for (Video video: videos) {
            for (String gen: video.getGenres()) {
                if (!popularGenresMap.containsKey(gen)) {
                    popularGenresMap.put(gen, numberOfViews(video, users.getUsers()));
                } else {
                    popularGenresMap.replace(gen, popularGenresMap.get(gen)
                            + numberOfViews(video, users.getUsers()));
                }
            }
        }

        ArrayList<TopGenres> popularGenres = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : popularGenresMap.entrySet()) {
            popularGenres.add(new TopGenres(entry.getKey(), entry.getValue()));
        }

        Comparator<TopGenres> cmpByPopularity = Comparator.comparingInt(TopGenres::getPopularity);
        popularGenres.sort(cmpByPopularity.reversed());

        assert auxUser != null;
        if (auxUser.getSubscriptionType().equals("PREMIUM")) {
            recommendResult = "PopularRecommendation cannot be applied!";

            for (Iterator<Video> it = videos.iterator(); it.hasNext();) {
                Video aux = it.next();
                if (auxUser.getHistory().containsKey(aux.getTitle())) {
                    it.remove();
                }
            }

            boolean checker = false;

            for (TopGenres gen: popularGenres) {
                for (Video video: videos) {
                    if (video.getGenres().contains(gen.getGenre())) {
                        recommendResult = "PopularRecommendation result: " + video.getTitle();
                        checker = true;
                        break;
                    }
                }
                if (checker) {
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
     * @param movies database
     * @param shows database
     * @param users database
     */
    public void favRec(final String username, final MovieDB movies, final ShowDB shows,
                       final UserDB users) {
        ArrayList<Video> videos = new ArrayList<>();
        videos.addAll(movies.getMovies());
        videos.addAll(shows.getShows());
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
            for (Iterator<Video> it = videos.iterator(); it.hasNext();) {
                Video aux = it.next();
                if (auxUser.getHistory().containsKey(aux.getTitle())) {
                    it.remove();
                }
            }

            for (Video aux: videos) {
                if (aux.nrOfFavorites(users) > favoriteNumber) {
                    favoriteNumber = aux.nrOfFavorites(users);
                }
            }

            recommendResult = "FavoriteRecommendation cannot be applied!";
            for (Video aux: videos) {
                if (aux.nrOfFavorites(users) == favoriteNumber) {
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
