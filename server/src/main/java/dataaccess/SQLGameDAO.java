package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

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
                        return readGame(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to get game: %s", e.getMessage()));
        }
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
    public Collection<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, white_username, black_username, name, game FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(readGame(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to get games: %s", e.getMessage()));
        }
        return games;
    }

    @Override
    public void updateGame(Integer gameID, String playerColor, String username) throws DataAccessException {
        if (playerColor.equals("WHITE")) {
            var statement = "UPDATE game SET white_username=? WHERE id=?";
            executeUpdate(statement, username, gameID);
        } else {
            var statement = "UPDATE game SET black_username=? WHERE id=?";
            executeUpdate(statement, username, gameID);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        executeUpdate("TRUNCATE game");
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String whiteUsername = rs.getString("white_username");
        String blackUsername = rs.getString("black_username");
        String name = rs.getString("name");
        var json = rs.getString("game");
        ChessGame game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(id, whiteUsername, blackUsername, name, game);
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
                    if (param instanceof Integer id) {
                        ps.setInt(i + 1, id);
                    }
                    else if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    }
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
