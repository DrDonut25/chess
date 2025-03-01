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

    public String createAuth(String username) throws DataAccessException {
        String authToken = generateToken();
        auths.put(username, new AuthData(username, authToken));
        return authToken;
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData getAuth(String authToken) {
        AuthData authData = null;
        return authData;
    }

    public void clear() {
        auths = new HashMap<String, AuthData>();
    }
}
