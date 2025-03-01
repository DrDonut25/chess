package dataaccess;

import model.AuthData;
import model.GameData;

public class SQLGameDAO implements GameDAO {
    @Override
    public void createGame(AuthData auth) {

    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public GameData[] listGames(AuthData auth) {
        return new GameData[0];
    }

    @Override
    public void updateGame(int gameID, String playerColor) {

    }

    @Override
    public void clear() {

    }
}
