package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO {
    private static Database db; //can this be properly updated?

    public static void createUser(String username, String password, String email) {
        //How to update database? Pointer instance object?
    }

    public static UserData getUser(String username) {
        UserData user = null;
        return user;
    }

    public static void clear() {

    }
}
