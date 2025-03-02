package dataaccess;

import model.GameData;

public class SQLGameDAO implements GameDAO {
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
}
