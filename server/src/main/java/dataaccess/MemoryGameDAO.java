package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private Map<Integer, GameData> games;

    public MemoryGameDAO() {
        games = new HashMap<Integer, GameData>();
    }

    public Map<Integer, GameData> getGames() {
        return games;
    }

    public Integer createGame(String gameName) {
        int gameID = generateGameID();
        games.put(gameID, new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    public int generateGameID() {
        return games.size() + 1;
    }

    public GameData getGame(Integer gameID) {
        return games.get(gameID);
    }

    public boolean colorIsTaken(String playerColor, Integer gameID) {
        if (playerColor.equals("WHITE")) {
            return getGame(gameID).whiteUsername() != null;
        } else {
            return getGame(gameID).blackUsername() != null;
        }
    }

    public GameData[] listGames() {
        GameData[] gameList = new GameData[games.size()];
        int i = 0;
        for (GameData game: games.values()) {
            gameList[i] = game;
            i++;
        }
        return gameList;
    }

    public void updateGame(Integer gameID, String playerColor, String username) {
        if (playerColor.equals("WHITE")) {
            GameData oldGame = games.get(gameID);
            String blackUsername = oldGame.blackUsername();
            String gameName = oldGame.gameName();
            ChessGame game = oldGame.game();
            games.put(gameID, new GameData(gameID, username, blackUsername, gameName, game));
        } else {
            GameData oldGame = games.get(gameID);
            String whiteUsername = oldGame.whiteUsername();
            String gameName = oldGame.gameName();
            ChessGame game = oldGame.game();
            games.put(gameID, new GameData(gameID, whiteUsername, username, gameName, game));
        }
    }

    public void clear() {
        games = new HashMap<Integer, GameData>();
    }
}
