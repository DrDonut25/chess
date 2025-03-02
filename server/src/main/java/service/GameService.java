package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import requestsresults.*;

public class GameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGameResult listGames(String authToken) {
        try {
            if (authDAO.getAuth(authToken) == null) {
                return new ListGameResult(null, "Error: unauthorized");
            }
            return new ListGameResult(gameDAO.listGames(), null);
        } catch (DataAccessException e) {
            return new ListGameResult(null, "Error: Data Access Exception");
        }
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
