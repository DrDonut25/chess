package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import requestsresults.*;

public class GameService {
    private GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
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

    public void clear() throws DataAccessException {
        gameDAO.clear();
    }
}
