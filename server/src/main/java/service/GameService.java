package service;

import dataaccess.Database;
import requestsresults.*;

public class GameService {
    private Database db;

    public GameService(Database db) {
        this.db = db;
    }

    public ListGameResult listGames(ListGameRequest listGameRequest) {
        ListGameResult listGameResult = null;
        return listGameResult;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        CreateGameResult createGameResult = null;
        return createGameResult;
    }

    public void joinGame(JoinGameRequest joinGameRequest) {

    }

    public void clearGames() {

    }
}
