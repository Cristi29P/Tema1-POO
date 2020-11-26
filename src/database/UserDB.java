package database;

import entities.User;
import java.util.ArrayList;

public final class UserDB {
    private final ArrayList<User> users;

    public UserDB(final ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
