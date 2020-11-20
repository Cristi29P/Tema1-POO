package database;

import entities.User;
import java.util.List;

public class UserDatabase {
    private List<User> users;

    public UserDatabase(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "UserDatabase{" +
                "users=" + users +
                '}';
    }
}
