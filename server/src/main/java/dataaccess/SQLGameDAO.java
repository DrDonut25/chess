package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        this.configureDatabase();
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException{
        var statement = "INSERT INTO game (name, game) VALUES (?, ?)";
        var json = new Gson().toJson(new ChessGame());
        return executeUpdate(statement, gameName, json);
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, white_username, black_username, name, game FROM game WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String whiteUsername = rs.getString("white_username");
                        String blackUsername = rs.getString("black_username");
                        String name = rs.getString("name");
                        ChessGame game = readGame(rs);
                        return new GameData(gameID, whiteUsername, blackUsername, name, game);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to get auth: %s", e.getMessage()));
        }
        return null;
    }

    private ChessGame readGame(ResultSet rs) throws SQLException {
        var json = rs.getString("game");
        return new Gson().fromJson(json, ChessGame.class);
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
                    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
                    white_username VARCHAR(255) DEFAULT NULL,
                    black_username VARCHAR(255) DEFAULT NULL,
                    name VARCHAR(255) NOT NULL,
                    game TEXT NOT NULL)""";
            try (var createTableStatement = conn.prepareStatement(createGameTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param  = params[i];
                    if (param instanceof Integer id) ps.setInt(i + 1, id);
                    else if (param instanceof String p) ps.setString(i + 1, p);
                }
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to update database: %s %s", statement, e.getMessage()));
        }
    }
}
