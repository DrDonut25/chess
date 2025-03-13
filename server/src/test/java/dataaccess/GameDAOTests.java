package dataaccess;

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

    }

    @Test
    @Order(6)
    public void listGamesFailed() {

    }

    @Test
    @Order(7)
    public void successUpdateGame() {

    }

    @Test
    @Order(8)
    public void updateGameFailed() {

    }

    @Test
    @Order(9)
    public void successClear() {

    }

}
