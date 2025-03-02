package dataaccess;

import model.AuthData;
import model.GameData;

public interface GameDAO {
    void createGame(AuthData auth) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    GameData[] listGames() throws DataAccessException;
    void updateGame(int gameID, String playerColor) throws DataAccessException;
    void clear() throws DataAccessException;
}
