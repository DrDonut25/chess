package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private Map<Integer, GameData> games;

    public static void createGame(AuthData authData) {

    }

    public static GameData getGame(String gameID) {
        GameData gameData = null;
        return gameData;
    }

    public static ArrayList<GameData> listGames(AuthData authData) {
        ArrayList<GameData> games = new ArrayList<>();
        return games;
    }

    public static void updateGame(String gameID, String playerColor) {

    }

    public static void clear() {

    }
}
