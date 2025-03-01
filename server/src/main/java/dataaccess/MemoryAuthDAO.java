package dataaccess;

import model.AuthData;

import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static Database db;

    public static String createAuth(String username) {
        String authToken = generateToken();
        db.addAuth(new AuthData(username, authToken));
        return authToken;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static AuthData getAuth(String authToken) {
        AuthData authData = null;
        return authData;
    }

    public static void clear() {

    }
}
