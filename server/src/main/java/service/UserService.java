package service;

import dataaccess.Database;
import dataaccess.MemoryUserDAO;
import model.UserData;
import requestsresults.LoginRequest;
import requestsresults.LoginResult;
import requestsresults.RegisterRequest;
import requestsresults.RegisterResult;

public class UserService {
    private Database db; //do I have to keep on passing this down until I reach a DAO class, then somehow kick it back up the chain?

    public UserService(Database db) {
        this.db = db;
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        String username = registerRequest.username();
        UserData userData = MemoryUserDAO.getUser(username);
        RegisterResult registerResult = null;
        return registerResult;
    }
    public LoginResult login(LoginRequest loginRequest) {
        LoginResult loginResult = null;
        return loginResult;
    }
    public void logout(String authToken) {

    }

    public void clearUsers() {
        //clear authData as well, as it is linked to UserData?

    }
}
