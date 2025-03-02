package dataaccess;

import model.GameData;

public interface GameDAO {
    Integer createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    GameData[] listGames() throws DataAccessException;
    void updateGame(int gameID, String playerColor) throws DataAccessException;
    void clear() throws DataAccessException;
}
