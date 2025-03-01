package dataaccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public String createAuth(String username) throws DataAccessException {
        return "";
    }

    @Override
    public String generateToken() {
        return "";
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
