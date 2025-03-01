package dataaccess;

public interface AuthDAO {
    String createAuth(String username) throws DataAccessException;
}
