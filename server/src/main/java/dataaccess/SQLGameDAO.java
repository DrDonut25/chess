package dataaccess;

import model.GameData;

import java.sql.SQLException;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        this.configureDatabase();
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException{
        return 0;
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public boolean colorIsTaken(String playerColor, Integer gameID) {
        return false;
    }

    @Override
    public GameData[] listGames() throws DataAccessException {
        return new GameData[0];
    }

    @Override
    public void updateGame(Integer gameID, String playerColor, String username) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            //NOTE: createGameTable still needs implementation for ChessGame
            String createGameTable = """
                    CREATE TABLE IF NOT EXISTS game (
                    id INTEGER NOT NULL,
                    white_username VARCHAR(255) NOT NULL,
                    black_username VARCHAR(255) NOT NULL,
                    name VARCHAR(255) NOT NULL,
                    game TEXT NOT NULL,
                    PRIMARY KEY ('id')
                    )""";
            try (var createTableStatement = conn.prepareStatement(createGameTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }
}
