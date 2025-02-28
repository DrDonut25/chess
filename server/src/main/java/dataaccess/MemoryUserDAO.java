package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO {
    public static void createUser(String username, String password, String email) {

    }

    public static UserData getUser(String username) {
        UserData user = null;
        return user;
    }

    public static void clear() {

    }
}
