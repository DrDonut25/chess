package dataaccess;

import model.AuthData;
import model.GameData;

public class SQLGameDAO implements GameDAO {
    @Override
    public void createGame(AuthData auth) throws DataAccessException{

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData[] listGames() throws DataAccessException {
        return new GameData[0];
    }

    @Override
    public void updateGame(int gameID, String playerColor) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
