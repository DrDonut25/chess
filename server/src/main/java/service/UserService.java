package service;

import dataaccess.*;
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
            String authToken = authDAO.createAuth(username);
            return new RegisterResult(username, authToken, null);
        } catch (DataAccessException e) {
            return new RegisterResult(null, null, "Error: Data Access Exception");
        }
    }
    public LoginResult login(LoginRequest loginRequest) {
        LoginResult loginResult = null;
        return loginResult;
    }
    public void logout(String authToken) {

    }
}
