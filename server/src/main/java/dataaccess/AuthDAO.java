package dataaccess;

import model.AuthData;

public interface AuthDAO {
    String createAuth(String username) throws DataAccessException;
    String generateToken();
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
}
