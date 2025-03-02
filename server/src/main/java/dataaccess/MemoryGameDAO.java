package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private Map<Integer, GameData> games;

    public MemoryGameDAO() {
        games = new HashMap<Integer, GameData>();
    }

    public Integer createGame(String gameName) {
        int gameID = generateGameID();
        games.put(gameID, new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    public int generateGameID() {
        return games.size() + 1;
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
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

    public void updateGame(int gameID, String playerColor) {

    }

    public void clear() {
        games = new HashMap<Integer, GameData>();
    }
}
