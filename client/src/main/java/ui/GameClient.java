package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import exception.DataAccessException;
import model.GameData;
import web.ServerMessageObserver;
import web.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Collection;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_YELLOW;

public class GameClient implements Client, ServerMessageObserver {
    private WebSocketFacade websocket;
    private final String authToken;
    private GameData gameData;
    private final boolean isObserving;
    private final boolean isWhiteOriented;

    public GameClient(String url, String auth, GameData game, boolean observing, boolean isWhite) throws DataAccessException {
        this.authToken = auth;
        this.gameData = game;
        this.isObserving = observing;
        this.isWhiteOriented = isWhite;
        //Make WebSocket connection
        websocket = new WebSocketFacade(url, this);
        websocket.connect(authToken, gameData.gameID());
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
        return BoardSketcher.drawBoard(isWhiteOriented, gameData.game(), null);
    }

    public String leave() throws DataAccessException {
        websocket.leave(authToken, gameData.gameID());
        websocket = null;
        return String.format("Left game %d", gameData.gameID());
    }

    public String makeMove(String[] params) throws DataAccessException {
        //Call makeMove method in WebSocketFacade — ensure input is valid
        if (params.length == 2) {
            String startPos = params[0];
            String endPos = params[1];
            ChessMove move = new ChessMove(toChessPosition(startPos), toChessPosition(endPos));
            websocket.makeMove(move, authToken, gameData.gameID());
            return String.format("Moved piece at %s to %s", startPos, endPos);
        } else {
            throw new DataAccessException("Error: invalid number of arguments — expected <START_POSITION> <END_POSITION>");
        }
    }

    private ChessPosition toChessPosition(String letterCoord) throws DataAccessException {
        letterCoord = letterCoord.toLowerCase();
        if (letterCoord.length() == 2) {
            int col;
            int row;
            if (letterCoord.charAt(0) == 'a') {
                col = 1;
            } else if (letterCoord.charAt(0) == 'b') {
                col = 2;
            } else if (letterCoord.charAt(0) == 'c') {
                col = 3;
            } else if (letterCoord.charAt(0) == 'd') {
                col = 4;
            } else if (letterCoord.charAt(0) == 'e') {
                col = 5;
            } else if (letterCoord.charAt(0) == 'f') {
                col = 6;
            } else if (letterCoord.charAt(0) == 'g') {
                col = 7;
            } else if (letterCoord.charAt(0) == 'h') {
                col = 8;
            } else {
                throw new DataAccessException("Error: invalid column letter — must be between a and h");
            }
            if (Character.isDigit(letterCoord.charAt(1))) {
                row = Character.getNumericValue(letterCoord.charAt(1));
            } else {
                throw new DataAccessException("Error: invalid row number — must be between 1 and 8");
            }
            return new ChessPosition(row, col);
        } else {
            throw new DataAccessException("Error: invalid position format — enter position in letter-number form (e.g. B6)");
        }
    }

    public String legalMoves(String[] params) throws DataAccessException {
        if (params.length == 1) {
            ChessPosition position = toChessPosition(params[0]);
            //Create Collection of ChessPositions that are legal positions to move to then pass to drawBoard
            Collection<ChessMove> legalMoves = gameData.game().validMoves(position);
            return BoardSketcher.drawBoard(isWhiteOriented, gameData.game(), legalMoves);
        } else {
            throw new DataAccessException("Error: invalid number of arguments — expected <POSITION>");
        }
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

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification((NotificationMessage) message);
            case ERROR -> displayError((ErrorMessage) message);
            case LOAD_GAME -> loadGame((LoadGameMessage) message);
        }
    }

    @Override
    public void displayNotification(NotificationMessage notification) {
        System.out.println(SET_TEXT_COLOR_YELLOW + notification.getMessage());
    }

    @Override
    public void displayError(ErrorMessage errorMessage) {
        System.out.println(SET_TEXT_COLOR_RED + errorMessage.getMessage());
    }

    @Override
    public void loadGame(LoadGameMessage gameMessage) {
        GameData gameData = gameMessage.getGame();
        this.gameData = gameData;
        ChessGame game = gameData.game();
        BoardSketcher.drawBoard(gameMessage.isWhiteOriented(), game, null);
    }
}
