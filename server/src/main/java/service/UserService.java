package service;

import dataaccess.*;
import model.UserData;
import requestsresults.*;

public class UserService {
    private AuthDAO authDAO; //do I have to keep on passing this down until I reach a DAO class, then somehow kick it back up the chain?
    private UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public void clear() {
        authDAO.clear();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws UserServiceException {
        String username = registerRequest.username();
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            return new RegisterResult(null, null, "Error: bad request");
        }
        if (userDAO.getUser(username) != null) {
            return new RegisterResult(null, null, "Error: already taken");
        }
        userDAO.createUser(username, registerRequest.password(), registerRequest.email());
        try {
            String authToken = authDAO.createAuth(username);
            return new RegisterResult(username, authToken, null);
        } catch (DataAccessException e) {
            return new RegisterResult(null, null, "Error: Data Access Exception");
        }
    }
    public LoginResult login(LoginRequest loginRequest) throws UserServiceException {
        LoginResult loginResult = null;
        return loginResult;
    }
    public void logout(String authToken) throws UserServiceException {

    }

    public ListGameResult listGames(ListGameRequest listGameRequest) throws GameServiceException {
        ListGameResult listGameResult = null;
        return listGameResult;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws GameServiceException {
        CreateGameResult createGameResult = null;
        return createGameResult;
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws GameServiceException {

    }
}
