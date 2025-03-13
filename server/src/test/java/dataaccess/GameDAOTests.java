package dataaccess;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

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

    }

    @Test
    @Order(2)
    public void createGameFailed() {

    }

    @Test
    @Order(3)
    public void successGetGame() {

    }

    @Test
    @Order(4)
    public void getGameFailed() {

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
