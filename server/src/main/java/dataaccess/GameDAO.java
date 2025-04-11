package dataaccess;

import chess.ChessGame;
import exception.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    Integer createGame(String gameName) throws DataAccessException;
    GameData getGame(Integer gameID) throws DataAccessException;
    boolean colorIsTaken(String playerColor, Integer gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void updateGame(Integer gameID, String playerColor, String username) throws DataAccessException;
    void updateBoard(Integer gameID, ChessGame game) throws DataAccessException;
    void clear() throws DataAccessException;
}
