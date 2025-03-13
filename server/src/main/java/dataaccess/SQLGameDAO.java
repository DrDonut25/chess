package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
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
    public boolean colorIsTaken(String playerColor, Integer gameID) throws DataAccessException {
        if (playerColor.equals("WHITE")) {
            return getGame(gameID).whiteUsername() != null;
        } else {
            return getGame(gameID).blackUsername() != null;
        }
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

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                for (int i = 0; i < params.length; i++) {
                    var param  = params[i];
                    if (param instanceof Integer id) ps.setInt(i + 1, id);
                    else if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof ChessGame game) {
                        var json = new Gson().toJson(game);
                        ps.setString(i + 1, json);
                    }
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to update database: %s %s", statement, e.getMessage()));
        }
    }
}
