package ui;

import exception.DataAccessException;

import java.util.Arrays;

public class GameClient implements Client {
    private final ServerFacade server;
    private String authToken;

    public GameClient(String serverUrl, String authToken) {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    @Override
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
