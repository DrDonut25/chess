package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import requestsresults.*;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

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
        try {
            if (createGameRequest.authToken() == null || createGameRequest.gameName() == null) {
                return new CreateGameResult(null, "Error: bad request");
            }
            if (authDAO.getAuth(createGameRequest.authToken()) == null) {
                return new CreateGameResult(null, "Error: unauthorized");
            }
            Integer gameID = gameDAO.createGame(createGameRequest.gameName());
            return new CreateGameResult(gameID, null);
        } catch (DataAccessException e) {
            return new CreateGameResult(null, "Error: Data Access Exception");
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest) {

    }

    public void clear() throws DataAccessException {
        gameDAO.clear();
    }
}
