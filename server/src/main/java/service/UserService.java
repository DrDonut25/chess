package service;

import dataaccess.MemoryUserDAO;
import model.UserData;
import requestsresults.LoginRequest;
import requestsresults.LoginResult;
import requestsresults.RegisterRequest;
import requestsresults.RegisterResult;

public class UserService {
    public static RegisterResult register(RegisterRequest registerRequest) {
        String username = registerRequest.username();
        UserData userData = MemoryUserDAO.getUser(username);
        RegisterResult registerResult = null;
        return registerResult;
    }
    public static LoginResult login(LoginRequest loginRequest) {
        LoginResult loginResult = null;
        return loginResult;
    }
    public static void logout(String authToken) {

    }

    public static void clearUsers() {
        //clear authData as well, as it is linked to UserData?

    }
}
