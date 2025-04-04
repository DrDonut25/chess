package web;

import exception.DataAccessException;
import requestsresults.*;

public class ServerFacade {
    private String serverUrl;
    private HttpCommunicator httpComm;
    private WebsocketCommunicator webComm;
    private ServerMessageObserver messageObserver;

    public ServerFacade (String serverUrl, ServerMessageObserver messageObserver) {
        this.serverUrl = serverUrl;
        httpComm = new HttpCommunicator(serverUrl);
        this.messageObserver = messageObserver;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        return httpComm.register(registerRequest);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        return httpComm.login(loginRequest);
    }

    public LogoutResult logout(String authToken) throws DataAccessException {
        return httpComm.logout(authToken);
    }

    public ListGameResult listGames(String authToken) throws DataAccessException {
        return httpComm.listGames(authToken);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        return httpComm.createGame(createGameRequest);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        httpComm.joinGame(joinGameRequest);
        //Websocket code?
        webComm = new WebsocketCommunicator(serverUrl, messageObserver);
    }

    public void clear() throws DataAccessException {
        httpComm.clear();
    }
}
