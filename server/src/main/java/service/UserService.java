package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import requestsresults.*;

public class UserService {
    private AuthDAO authDAO;
    private UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public void clear() throws DataAccessException {
        authDAO.clear();
        userDAO.clear();
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        try {
            String username = registerRequest.username();
            if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
                return new RegisterResult(null, null, "Error: bad request");
            }
            if (userDAO.getUser(username) != null) {
                return new RegisterResult(null, null, "Error: already taken");
            }
            userDAO.createUser(username, registerRequest.password(), registerRequest.email());
            return new RegisterResult(username, authDAO.createAuth(username), null);
        } catch (DataAccessException e) {
            return new RegisterResult(null, null, "Error: Data Access Exception");
        }
    }
    public LoginResult login(LoginRequest loginRequest) {
        try {
            String username = loginRequest.username();
            if (userDAO.getUser(username) == null || !userDAO.getUser(username).password().equals(loginRequest.password())) {
                return new LoginResult(null, null, "Error: unauthorized");
            }
            return new LoginResult(username, authDAO.createAuth(username), null);
        } catch (DataAccessException e) {
            return new LoginResult(null, null, "Error: Data Access Exception");
        }
    }

    public LogoutResult logout(String authToken) {
        try {
            if (authDAO.getAuth(authToken) == null) {
                return new LogoutResult("Error: unauthorized");
            }
            authDAO.deleteAuth(authToken);
            return new LogoutResult("");
        } catch (DataAccessException e) {
            return new LogoutResult("Error: Data Access Exception");
        }
    }
}
