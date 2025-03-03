package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import requestsresults.*;

public class ServiceTests {
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

    @Test
    @Order(9)
    public void successGameClear() {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        GameService gameService = new GameService(authDAO, gameDAO);
        Assertions.assertDoesNotThrow(gameService::clear);
    }

    @Test
    @Order(10)
    public void gameClearFailed() {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameService gameService = new GameService(authDAO, null);
        Assertions.assertThrows(NullPointerException.class, gameService::clear);
    }

    @Test
    @Order(11)
    public void successListGames(){
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        GameService gameService = new GameService(authDAO, gameDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", "myEmail");
        String authToken = userService.register(regReq).authToken();
        gameService.createGame(new CreateGameRequest(authToken, "myGame"));
        Assertions.assertNull(gameService.listGames(authToken).message());
    }

    @Test
    @Order(12)
    public void listGamesFailed(){
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        GameService gameService = new GameService(authDAO, gameDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", "myEmail");
        String authToken = userService.register(regReq).authToken();
        gameService.createGame(new CreateGameRequest(authToken, "myGame"));
        Assertions.assertEquals(gameService.listGames("badAuthToken").message(), "Error: unauthorized");
    }

    @Test
    @Order(13)
    public void successCreateGame(){
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        GameService gameService = new GameService(authDAO, gameDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", "myEmail");
        String authToken = userService.register(regReq).authToken();
        Assertions.assertNull(gameService.createGame(new CreateGameRequest(authToken, "myGame")).message());
    }

    @Test
    @Order(14)
    public void createGameFailed(){
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        GameService gameService = new GameService(authDAO, gameDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", "myEmail");
        userService.register(regReq);
        CreateGameRequest creReq = new CreateGameRequest("badAuthToken", "myGame");
        Assertions.assertEquals(gameService.createGame(creReq).message(), "Error: unauthorized");
    }

    @Test
    @Order(15)
    public void successJoinGame(){
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        GameService gameService = new GameService(authDAO, gameDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", "myEmail");
        String authToken = userService.register(regReq).authToken();
        Integer gameID = gameService.createGame(new CreateGameRequest(authToken, "myGame")).gameID();
        JoinGameRequest joinReq = new JoinGameRequest(authToken, "WHITE", gameID);
        Assertions.assertEquals(gameService.joinGame(joinReq).message(), "");
    }

    @Test
    @Order(16)
    public void joinGameFailed(){
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(authDAO, userDAO);
        GameService gameService = new GameService(authDAO, gameDAO);
        RegisterRequest regReq = new RegisterRequest("myUsername", "myPassword", "myEmail");
        String authToken = userService.register(regReq).authToken();
        JoinGameRequest joinReq = new JoinGameRequest(authToken, "WHITE", 155);
        Assertions.assertEquals(gameService.joinGame(joinReq).message(), "Error: bad request");
    }
}
