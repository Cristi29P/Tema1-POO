package Actions;

import entities.User;

import java.util.ArrayList;

public class CommandExecutor {
    private String commandResult;

    public void addFavorite (String username, String title, ArrayList<User> users) {
        for(User user: users) {
            if(user.getUsername().equals(username)) {
                if(user.getFavoriteMovies().contains(title)) {
                    commandResult = "error -> " + title + " is already in favourite list";
                } else {
                    user.getFavoriteMovies().add(title);
                    commandResult = "success -> " + title + " was added as favourite";
                }
            }
        }
    }

    public String getCommandResult() {
        return commandResult;
    }
}
