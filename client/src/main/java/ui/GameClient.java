package ui;

import exception.DataAccessException;
import web.ServerFacade;
import web.ServerMessageObserver;
import web.WebSocketFacade;

import java.util.Arrays;

public class GameClient implements Client {
    private final WebSocketFacade websocket;
    private String authToken;

    public GameClient(String serverUrl, String authToken, ServerMessageObserver messageObserver) throws DataAccessException {
        websocket = new WebSocketFacade(serverUrl, messageObserver);
        this.authToken = authToken;
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
            return switch (cmd) {
                case "redraw" -> redraw(params);
                case "leave" -> leave();
                case "make_move" -> makeMove(params);
                case "legal_moves" -> legalMoves(params);
                case "resign" -> resign();
                case "quit" -> "Exiting program";
                default -> help();
            };
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }

    public String redraw(String[] params) throws DataAccessException {
        return "";
    }

    public String leave() throws DataAccessException {
        return "";
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
        return """
                redraw - draw current game board
                leave - leave current game
                make_move <START_POSITION> <END_POSITION> - move chess piece
                legal_moves <POSITION> - highlights legal moves at specified position
                resign - forfeit game (will not leave game)
                quit - exit program
                help - list possible commands
                """;
    }
}
