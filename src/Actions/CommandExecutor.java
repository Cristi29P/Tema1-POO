package Actions;

import entities.User;

import java.util.ArrayList;

public class CommandExecutor {
    private String commandResult;

    public void addFavorite(String username, String title, ArrayList<User> users) {
        for(User user: users) {
            if(user.getUsername().equals(username)) {
                if(user.getHistory().containsKey(title) && user.getFavoriteMovies().contains(title)) {
                    commandResult = "error -> " + title + " is already in favourite list";
                } else if (user.getHistory().containsKey(title) && !(user.getFavoriteMovies().contains(title))) {
                    user.getFavoriteMovies().add(title);
                    commandResult = "success -> " + title + " was added as favourite";
                } else {
                    commandResult = "error -> " + title + " is not seen";
                }
            }
        }
    }

    public void addView(String username, String title, ArrayList<User> users) {
        for(User user: users) {
            if(user.getUsername().equals(username)) {
                if(user.getHistory().containsKey(title)) {
                    user.getHistory().replace(title, user.getHistory().get(title) + 1);
                    commandResult = "success -> " + title + " was viewed with total views of " + user.getHistory().get(title);
                } else {
                    user.getHistory().put(title, 1);
                    commandResult = "success -> " + title + " was viewed with total views of " + user.getHistory().get(title);
                }
            }
        }
    }

    public String getCommandResult() {
        return commandResult;
    }
}
