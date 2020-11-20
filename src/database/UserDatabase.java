package database;

import entities.User;
import java.util.ArrayList;

public class UserDatabase {
    private ArrayList<User> users;

    public UserDatabase(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
