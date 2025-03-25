import exception.DataAccessException;
import org.junit.jupiter.api.*;
import requestsresults.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverURL = "http://localhost:" + port;
        facade = new ServerFacade(serverURL);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @AfterEach
    void clear() {
        try {
            facade.clear();
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(0)
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    @Order(1)
    public void successRegister() {
        try {
            RegisterRequest regReq = new RegisterRequest("Fred", "Fredrocks", "fred@email.com");
            RegisterResult regRes = facade.register(regReq);
            Assertions.assertFalse(regRes.authToken().isEmpty());
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void registerFailed() {
        RegisterRequest regReq = new RegisterRequest("Fred", "Fredrocks", null);
        Assertions.assertThrows(DataAccessException.class, () -> facade.register(regReq));
    }

    @Test
    @Order(3)
    public void successLogin() {
        try {
            RegisterRequest regReq = new RegisterRequest("Fred", "Fredrocks", "fred@email.com");
            RegisterResult regRes = facade.register(regReq);
            facade.logout(regRes.authToken());
            LoginRequest logReq = new LoginRequest("Fred", "Fredrocks");
            Assertions.assertNull(facade.login(logReq).message());
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void loginFailed() {
        LoginRequest logReq = new LoginRequest("Fred", "Fredrocks");
        Assertions.assertThrows(DataAccessException.class, () -> facade.login(logReq).message());
    }

    @Test
    @Order(5)
    public void successLogout() {
        try {
            RegisterRequest regReq = new RegisterRequest("Fred", "Fredrocks", "fred@email.com");
            RegisterResult regRes = facade.register(regReq);
            Assertions.assertDoesNotThrow(() -> facade.logout(regRes.authToken()));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(6)
    public void logoutFailed() {
        Assertions.assertThrows(DataAccessException.class, () -> facade.logout("badAuth"));
    }

    @Test
    @Order(7)
    public void successListGames() {
        try {
            RegisterRequest regReq = new RegisterRequest("Fred", "Fredrocks", "fred@email.com");
            RegisterResult regRes = facade.register(regReq);
            Assertions.assertDoesNotThrow(() -> facade.listGames(regRes.authToken()));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(8)
    public void listGamesFailed() {
        Assertions.assertThrows(DataAccessException.class, () -> facade.listGames("badAuth"));
    }

    @Test
    @Order(9)
    public void successCreateGame() {
        try {
            RegisterRequest regReq = new RegisterRequest("Fred", "Fredrocks", "fred@email.com");
            RegisterResult regRes = facade.register(regReq);
            String authToken = regRes.authToken();
            CreateGameRequest createReq = new CreateGameRequest(authToken, "FredGame");
            facade.createGame(createReq);
            Assertions.assertNotEquals(facade.listGames(authToken).games().size(), 0);
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(10)
    public void createGameFailed() {
        CreateGameRequest createReq = new CreateGameRequest("badAuth", "myGame");
        Assertions.assertThrows(DataAccessException.class, () -> facade.createGame(createReq));
    }

    @Test
    @Order(11)
    public void successJoinGame() {
        try {
            RegisterRequest regReq = new RegisterRequest("Fred", "Fredrocks", "fred@email.com");
            RegisterResult regRes = facade.register(regReq);
            String authToken = regRes.authToken();
            CreateGameRequest createReq = new CreateGameRequest(authToken, "FredGame");
            CreateGameResult createRes = facade.createGame(createReq);
            JoinGameRequest joinReq = new JoinGameRequest(authToken,"WHITE", createRes.gameID());
            Assertions.assertDoesNotThrow(() -> facade.joinGame(joinReq));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(12)
    public void joinGameFailed() {
        try {
            RegisterRequest regReq = new RegisterRequest("Fred", "Fredrocks", "fred@email.com");
            RegisterResult regRes = facade.register(regReq);
            String authToken = regRes.authToken();
            CreateGameRequest createReq = new CreateGameRequest(authToken, "FredGame");
            CreateGameResult createRes = facade.createGame(createReq);
            JoinGameRequest joinReq = new JoinGameRequest(authToken,"BLUE", createRes.gameID());
            Assertions.assertThrows(DataAccessException.class, () -> facade.joinGame(joinReq));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(13)
    public void successClear() {
        Assertions.assertDoesNotThrow(facade::clear);
    }
}
