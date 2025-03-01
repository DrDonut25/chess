package service;

import dataaccess.Database;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import requestsresults.*;

public class UserService {
    private Database db; //do I have to keep on passing this down until I reach a DAO class, then somehow kick it back up the chain?

    public UserService(Database db) {
        this.db = db;
    }

    public void clear() {
        db.clearUsers();
        db.clearAuths();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws UserServiceException {
        String username = registerRequest.username();
        if (MemoryUserDAO.getUser(username) != null) {
            throw new UserServiceException(403, "Error: already taken");
        }
        MemoryUserDAO.createUser(username, registerRequest.password(), registerRequest.email());
        String authToken = MemoryAuthDAO.createAuth(username);
        return new RegisterResult(username, authToken);
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
