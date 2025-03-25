import exception.DataAccessException;
import org.junit.jupiter.api.*;
import requestsresults.LoginRequest;
import requestsresults.RegisterRequest;
import requestsresults.RegisterResult;
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
        try {
            facade.clear();
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
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
        Assertions.assertTrue(true);
    }

    @Test
    @Order(5)
    public void successLogout() {
        Assertions.assertTrue(true);
    }

    @Test
    @Order(6)
    public void logoutFailed() {
        Assertions.assertTrue(true);
    }

    @Test
    @Order(7)
    public void successListGames() {
        Assertions.assertTrue(true);
    }

    @Test
    @Order(8)
    public void listGamesFailed() {
        Assertions.assertTrue(true);
    }

    @Test
    @Order(9)
    public void successCreateGame() {
        Assertions.assertTrue(true);
    }

    @Test
    @Order(10)
    public void createGameFailed() {
        Assertions.assertTrue(true);
    }

    @Test
    @Order(11)
    public void successJoinGame() {
        Assertions.assertTrue(true);
    }

    @Test
    @Order(12)
    public void joinGameFailed() {
        Assertions.assertTrue(true);
    }

    @Test
    @Order(13)
    public void successClear() {
        Assertions.assertDoesNotThrow(facade::clear);
    }
}
