package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        this.configureDatabase();
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            String createUserTable = """
                    CREATE TABLE IF NOT EXISTS user (
                    username VARCHAR(255) NOT NULL;
                    password VARCHAR(255) NOT NULL;
                    email VARCHAR(255) NOT NULL;
                    )""";
            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }
}
