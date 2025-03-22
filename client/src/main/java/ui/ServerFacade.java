package ui;

import exception.DataAccessException;
import requestsresults.*;

import javax.xml.crypto.Data;

public class ServerFacade {
    int port;

    public ServerFacade (int port) {
        this.port = port;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        return null;
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        return null;
    }

    public LogoutResult logout(String authToken) throws DataAccessException {
        return null;
    }

    public ListGameResult listGames(String authToken) throws DataAccessException {
        return null;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        return null;
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        return null;
    }
}
