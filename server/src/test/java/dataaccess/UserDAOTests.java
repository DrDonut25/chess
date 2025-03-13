package dataaccess;

import org.junit.jupiter.api.*;

public class UserDAOTests {
    static UserDAO userDAO;

    @BeforeAll
    public static void createUserDAO() {
        try {
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @AfterAll
    public static void clearUsers() {
        try {
            userDAO.clear();
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(1)
    public void successCreateUser() {
        Assertions.assertDoesNotThrow(() -> userDAO.createUser("myUsername", "Password123", "myEmail"));
    }

    @Test
    @Order(2)
    public void createUserFailed() {
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser("myUsername", "Password123", null));
    }

    @Test
    @Order(3)
    public void successGetUser() {
        try {
            userDAO.createUser("myUsername", "Password123", "myEmail");
            Assertions.assertNotNull(userDAO.getUser("myUsername"));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void getUserFailed() {
        try {
            userDAO.createUser("myUsername", "Password123", "myEmail");
            Assertions.assertNull(userDAO.getUser("badUsername"));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void successClear() {
        Assertions.assertDoesNotThrow(userDAO::clear);
    }
}
