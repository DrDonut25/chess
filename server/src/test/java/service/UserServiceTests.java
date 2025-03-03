package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import requestsresults.*;

public class UserServiceTests {
    @Test
    @Order(1)
    public void successUserClear() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        Assertions.assertDoesNotThrow(userService::clear);
    }

    @Test
    @Order(2)
    public void userClearFailed() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserService userService = new UserService(authDAO, null);
        Assertions.assertThrows(NullPointerException.class, userService::clear);
    }

    @Test
    @Order(3)
    public void successRegister() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", "myEmail");
        Assertions.assertNull(userService.register(regReq).message());
    }

    @Test
    @Order(4)
    public void registerFailed() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", null);
        Assertions.assertEquals(userService.register(regReq).message(), "Error: bad request");
    }

    @Test
    @Order(5)
    public void successLogin() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", "myEmail");
        userService.register(regReq);
        LoginRequest logReq = new LoginRequest("myUsername", "myPassword");
        Assertions.assertNull(userService.login(logReq).message());
    }

    @Test
    @Order(6)
    public void loginFailed() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", "myEmail");
        userService.register(regReq);
        LoginRequest logReq = new LoginRequest("myUsername", "badPassword");
        Assertions.assertEquals(userService.login(logReq).message(), "Error: unauthorized");
    }

    @Test
    @Order(7)
    public void successLogout() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", "myEmail");
        RegisterResult regRes = userService.register(regReq);
        Assertions.assertEquals(userService.logout(regRes.authToken()), new LogoutResult(""));
    }

    @Test
    @Order(8)
    public void logoutFailed() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", "myEmail");
        RegisterResult regRes = userService.register(regReq);
        userService.logout(regRes.authToken());
        Assertions.assertEquals(userService.logout(regRes.authToken()).message(), "Error: unauthorized");
    }
}
