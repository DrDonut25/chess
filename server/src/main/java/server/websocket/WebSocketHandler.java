package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.gameService = new GameService(authDAO, gameDAO);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        try {
            UserGameCommand gameCommand = new Gson().fromJson(msg, UserGameCommand.class);
            String username = getUsername(gameCommand.getAuthToken());
            switch (gameCommand.getCommandType()) {
                case CONNECT -> connect(session, username, gameCommand);
                case MAKE_MOVE -> makeMove(session, username, new Gson().fromJson(msg, MakeMoveCommand.class));
                case LEAVE -> leaveGame(username, gameCommand);
                case RESIGN -> resign(username, gameCommand);
            }
        } catch (DataAccessException e) {
            sendMessage(session.getRemote(), new ErrorMessage(e.getMessage()));
        }
    }

    private void sendMessage(RemoteEndpoint remoteEndpoint, ErrorMessage errorMessage) throws IOException {
        remoteEndpoint.sendString(new Gson().toJson(errorMessage));
    }

    private String getUsername(String authToken) throws DataAccessException {
        return gameService.getAuth(authToken).username();
    }

    public void connect(Session session, String username, UserGameCommand command) throws DataAccessException {
        try {
            //Get GameData
            Integer gameID = command.getGameID();
            GameData gameData = gameService.getGame(gameID);
            //Figure out board orientation
            boolean isWhiteOriented = !gameData.blackUsername().equals(username);
            //Create/send LoadGameMessage back to messaging client
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData, isWhiteOriented);
            session.getRemote().sendString(new Gson().toJson(loadGameMessage));
            //Add user to Connection Map
            connections.add(gameID, username, session);
            //Notify all OTHER clients that user joined the game
            String message = username + " joined the game";
            NotificationMessage notification = new NotificationMessage(message);
            connections.broadcast(username, gameID, notification);
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void makeMove(Session session, String username, MakeMoveCommand command) throws DataAccessException {
        //Get ChessMove and convert coordinates to letter-number format
        ChessMove move = command.getMove();
        ChessPosition pos1 = move.getStartPosition();
        ChessPosition pos2 = move.getEndPosition();
        String startPos = pos1.toCoordString();
        String endPos = pos2.toCoordString();
        Integer gameID = command.getGameID();

        //Get GameData
        GameData gameData = gameService.getGame(gameID);
        ChessGame game = gameData.game();
        //Prevent observers or out of turn players from making a move
        ChessGame.TeamColor teamTurn = game.getTeamTurn();
        if (gameData.whiteUsername().equals(username)) {
            if (teamTurn != ChessGame.TeamColor.WHITE) {
                throw new DataAccessException("Error: It is not white's turn to make a move");
            }
        } else if (gameData.blackUsername().equals(username)) {
            if (teamTurn != ChessGame.TeamColor.BLACK) {
                throw new DataAccessException("Error: It is not black's turn to make a move");
            }
        } else {
            throw new DataAccessException("Error: Observers cannot make moves");
        }
        //Make sure move is valid-- Call makeMove in Phase 1 code
        try {
            game.makeMove(move);
        } catch (InvalidMoveException e) {
            throw new DataAccessException(e.getMessage());
        }
        //Update ChessGame
        gameService.updateBoard(gameID, game);
        //Figure out boardOrientation
        boolean isWhiteOriented = !gameData.blackUsername().equals(username);
        //Send relevant ServerMessages
        try {
            //Send LoadGameMessage to ALL clients
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData, isWhiteOriented);
            session.getRemote().sendString(new Gson().toJson(loadGameMessage));
            connections.broadcast(username, gameID, loadGameMessage);
            //Send NotificationMessage to all OTHER clients spelling out what move was made
            String moveMessage = String.format("%s made a move: %s to %s", username, startPos, endPos);
            NotificationMessage moveNotif = new NotificationMessage(moveMessage);
            connections.broadcast(username, gameID, moveNotif);
            //Notify ALL clients if in check/checkmate/stalemate
            if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                notifyCheckMessage("White is in check!", gameID);
            } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                notifyCheckMessage("Black is in check!", gameID);
            } else if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                notifyCheckMessage("Checkmate! Black wins!", gameID);
            } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                notifyCheckMessage("Checkmate! White wins!", gameID);
            } else if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
                notifyCheckMessage("Stalemate! Black wins!", gameID);
            } else if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                notifyCheckMessage("Stalemate! White wins!", gameID);
            }
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void notifyCheckMessage(String message, Integer gameID) throws IOException {
        NotificationMessage checkNotification = new NotificationMessage(message);
        connections.broadcast(null, gameID, checkNotification);
    }

    public void leaveGame(String username, UserGameCommand command) throws DataAccessException {
        try {
            //Remove user from ChessGame (if client is observer, do nothing
            Integer gameID = command.getGameID();
            GameData gameData = gameService.getGame(gameID);
            if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
                gameService.updateGame(gameID, "WHITE", null);
            } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)) {
                gameService.updateGame(gameID, "BLACK", null);
            }
            //Remove client's connection from ConnectionManager
            connections.remove(username);

            //Notify other clients that user left the game
            String message = username + " left the game";
            NotificationMessage notification = new NotificationMessage(message);
            connections.broadcast(username, gameID, notification);
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void resign(String username, UserGameCommand command) throws DataAccessException {
        //Get game
        Integer gameID = command.getGameID();
        GameData gameData = gameService.getGame(gameID);
        ChessGame game = gameData.game();
        //Prevent observers from resigning
        if (!gameData.whiteUsername().equals(username) && !gameData.blackUsername().equals(username)) {
            throw new DataAccessException("Error: Observers cannot resign/forfeit the game");
        }
        //Prevent players from resigning twice
        if (game.isGameOver()) {
            throw new DataAccessException("Error: Game has already been forfeited");
        }
        game.resign();
        //End ChessGame—do NOT remove the resigning user
        gameService.updateBoard(gameID, game);
        try {
            //Notify ALL clients that game has been forfeited
            String message = username + " forfeited the game!";
            NotificationMessage notification = new NotificationMessage(message);
            connections.broadcast(null, gameID, notification);
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
