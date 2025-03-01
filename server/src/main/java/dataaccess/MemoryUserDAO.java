package dataaccess;

import model.UserData;
import service.UserServiceException;

public class MemoryUserDAO implements UserDAO {
    private static Database db; //can this be properly updated if it's static? Should it be a pointer object?

    public static void createUser(String username, String password, String email) {
        //How to update database?
        UserData user = new UserData(username, password, email);
        db.addUser(user);
    }

    public static UserData getUser(String username) {
        return db.getUsers().get(username);
    }

    public static void clear() {
        db.clearUsers();
    }
}
