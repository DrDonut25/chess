package ui;

import exception.DataAccessException;
import model.GameData;
import web.ServerMessageObserver;
import web.WebSocketFacade;

import java.util.Arrays;

public class GameClient implements Client {
    private WebSocketFacade websocket;
    private final String authToken;
    private GameData gameData;
    private final boolean isObserving;
    private boolean isWhiteOriented;

    public GameClient(String url, String auth, GameData game, ServerMessageObserver observer, boolean observing, boolean isWhite) throws DataAccessException {
        this.authToken = auth;
        this.gameData = game;
        this.isObserving = observing;
        this.isWhiteOriented = isWhite;
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
                    case "redraw" -> redraw();
                    case "leave" -> leave();
                    case "make_move" -> makeMove(params);
                    case "legal_moves" -> legalMoves(params);
                    case "resign" -> resign();
                    case "quit" -> "Exiting program";
                    default -> help();
                };
            } else {
                return switch (cmd) {
                    case "redraw" -> redraw();
                    case "leave" -> leave();
                    case "quit" -> "Exiting program";
                    default -> help();
                };
            }
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }

    public String redraw() throws DataAccessException {
        //Can I use my approach from phase 5? Or do I need to ask for a LoadGameMessage?
        return BoardSketcher.drawBoard(isWhiteOriented, gameData.game());
    }

    public String leave() throws DataAccessException {
        websocket.leave(authToken, gameData.gameID());
        websocket = null;
        return String.format("Left game %d", gameData.gameID());
    }

    public String makeMove(String[] params) throws DataAccessException {
        //Call makeMove method in WebSocketFacade
        //Update ChessGame to reflect the move you just made (also WebSocketFacade?)
        //Broadcast to all players/observers that you made a move (also WebSocketFacade?)
        //Redraw updated board?
        return "";
    }

    public String legalMoves(String[] params) throws DataAccessException {
        //Maybe create additional method in BoardSketcher class that handles printing board with highlighted tiles
        //Create Collection of ChessPositions that are valid positions to move to, then pass to BoardSketcher class ^

        return "";
    }

    public String resign() throws DataAccessException {
        //End game. Consider adding gameOver boolean to ChessGame class
        //Do above step in WebSocketFacade?
        websocket.resign(authToken, gameData.gameID());
        return "Resigned the game. You lose!";
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
