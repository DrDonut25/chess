package dataaccess;

import exception.DataAccessException;
import org.junit.jupiter.api.*;

public class AuthDAOTests {
    private static AuthDAO authDAO;

    @BeforeAll
    public static void createAuthDAO() {
        try {
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @AfterAll
    public static void clearAuths() {
        try {
            authDAO.clear();
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(1)
    public void successCreateAuth() {
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth("myUsername"));
    }

    @Test
    @Order(2)
    public void createAuthFailed() {
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(null));
    }

    @Test
    @Order(3)
    public void successGetAuth() {
        try {
            String authToken = authDAO.createAuth("myUsername");
            Assertions.assertNotNull(authDAO.getAuth(authToken));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void getAuthFailed() {
        try {
            authDAO.createAuth("myUsername");
            Assertions.assertNull(authDAO.getAuth("badAuth"));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void successDeleteAuth() {
        try {
            String authToken = authDAO.createAuth("myUsername");
            Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(authToken));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(6)
    public void deleteAuthFailed() {
        try {
            authDAO.createAuth("myUsername");
            Assertions.assertThrows(DataAccessException.class, () -> authDAO.deleteAuth(null));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(7)
    public void successClear() {
        Assertions.assertDoesNotThrow(authDAO::clear);
    }
}
