package dataaccess;

import model.AuthData;
import model.GameData;

public interface GameDAO {
    void createGame(AuthData auth);
    GameData getGame(int gameID);
    GameData[] listGames(AuthData auth);
    void updateGame(int gameID, String playerColor);
    void clear();
}
