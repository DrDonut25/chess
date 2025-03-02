package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private Map<String, AuthData> auths;

    public MemoryAuthDAO() {
        auths = new HashMap<String, AuthData>();
    }

    public String createAuth(String username) {
        String authToken = generateToken();
        auths.put(authToken, new AuthData(username, authToken));
        return authToken;
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    public void clear() {
        auths = new HashMap<String, AuthData>();
    }
}
