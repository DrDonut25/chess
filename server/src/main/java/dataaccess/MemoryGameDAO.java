package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private Map<Integer, GameData> games;

    public void createGame(AuthData auth) {
        games = new HashMap<Integer, GameData>();
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
