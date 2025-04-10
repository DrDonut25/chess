package ui;

import exception.DataAccessException;
import model.GameData;
import web.ServerFacade;
import web.ServerMessageObserver;
import web.WebSocketFacade;

import java.util.Arrays;

public class GameClient implements Client {
    private WebSocketFacade websocket;
    private String authToken;
    private GameData game;
    private boolean isObserving;

    public GameClient(String url, String auth, GameData game, ServerMessageObserver observer, boolean observing) throws DataAccessException {
        this.authToken = auth;
        this.game = game;
        this.isObserving = observing;
        websocket = new WebSocketFacade(url, observer);
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (!isObserving) {
                return switch (cmd) {
                    case "redraw" -> redraw(params);
                    case "leave" -> leave();
                    case "make_move" -> makeMove(params);
                    case "legal_moves" -> legalMoves(params);
                    case "resign" -> resign();
                    case "quit" -> "Exiting program";
                    default -> help();
                };
            } else {
                return switch (cmd) {
                    case "redraw" -> redraw(params);
                    case "leave" -> leave();
                    case "quit" -> "Exiting program";
                    default -> help();
                };
            }
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }

    public String redraw(String[] params) throws DataAccessException {
        return "";
    }

    public String leave() throws DataAccessException {
        websocket.leave(authToken, game.gameID());
        websocket = null;
        return String.format("Left game %d", game.gameID());
    }

    public String makeMove(String[] params) throws DataAccessException {
        return "";
    }

    public String legalMoves(String[] params) throws DataAccessException {
        return "";
    }

    public String resign() throws DataAccessException {
        return "";
    }

    @Override
    public String help() {
        if (!isObserving) {
            return """
                redraw - draw current game board
                leave - leave current game
                make_move <START_POSITION> <END_POSITION> - move chess piece
                legal_moves <POSITION> - highlights legal moves at specified position
                resign - forfeit game (will not leave game)
                quit - exit program
                help - list possible commands
                """;
        } else {
            return """
                redraw - draw current game board
                leave - stop observing current game
                quit - exit program
                help - list possible commands
                """;
        }
    }
}
