package dataaccess;

import exception.DataAccessException;
import org.junit.jupiter.api.*;

public class GameDAOTests {
    private static GameDAO gameDAO;

    @BeforeAll
    public static void createGameDAO() {
        try {
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @AfterAll
    public static void clearGames() {
        try {
            gameDAO.clear();
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(1)
    public void successCreateGame() {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("myGame"));
    }

    @Test
    @Order(2)
    public void createGameFailed() {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    @Order(3)
    public void successGetGame() {
        try {
            Integer gameID = gameDAO.createGame("myGame");
            Assertions.assertNotNull(gameDAO.getGame(gameID));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(4)
    public void getGameFailed() {
        try {
            gameDAO.createGame("myGame");
            Assertions.assertNull(gameDAO.getGame(11));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void successListGames() {
        try {
            gameDAO.createGame("gameOne");
            gameDAO.createGame("gameTwo");
            Assertions.assertNotEquals(0, gameDAO.listGames().size());
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(6)
    public void listGamesFailed() {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    @Order(7)
    public void successUpdateGame() {
        try {
            Integer gameID = gameDAO.createGame("myGame");
            Assertions.assertDoesNotThrow(() -> gameDAO.updateGame(gameID, "WHITE", "myUsername"));
        } catch (DataAccessException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    @Order(8)
    public void updateGameFailed() {
        Assertions.assertThrows(NullPointerException.class, () -> gameDAO.updateGame(5, null, null));
    }

    @Test
    @Order(9)
    public void successClear() {
        Assertions.assertDoesNotThrow(gameDAO::clear);
    }

}
