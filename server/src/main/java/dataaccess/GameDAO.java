package dataaccess;

import model.GameData;

import java.util.Map;

public interface GameDAO {
    Integer createGame(String gameName) throws DataAccessException;
    GameData getGame(Integer gameID) throws DataAccessException;
    boolean colorIsTaken(String playerColor, Integer gameID);
    GameData[] listGames() throws DataAccessException;
    void updateGame(Integer gameID, String playerColor, String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
