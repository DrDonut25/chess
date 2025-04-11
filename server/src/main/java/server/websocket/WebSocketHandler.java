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
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private ConnectionManager connections = new ConnectionManager();
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        try {
            UserGameCommand gameCommand = new Gson().fromJson(msg, UserGameCommand.class);
            String username = getUsername(gameCommand.getAuthToken());
            switch (gameCommand.getCommandType()) {
                case CONNECT -> connect(session, username, gameCommand);
                case MAKE_MOVE -> makeMove(session, username, new Gson().fromJson(msg, MakeMoveCommand.class));
                case LEAVE -> leaveGame(session, username, gameCommand);
                case RESIGN -> resign(session, username, gameCommand);
            }
        } catch (DataAccessException e) {
            session.getRemote().sendString("Error: Unauthorized");
        }
    }

    private String getUsername(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken).username();
    }

    public void connect(Session session, String username, UserGameCommand command) throws DataAccessException {
        try {
            //Get GameData
            Integer gameID = command.getGameID();
            GameData gameData = gameDAO.getGame(gameID);
            //Figure out board orientation
            boolean isWhiteOriented = !gameData.blackUsername().equals(username);
            //Create/send LoadGameMessage back to messaging client
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData, isWhiteOriented);
            session.getRemote().sendString(new Gson().toJson(loadGameMessage));
            //Add user to Connection Map
            connections.add(username, session);
            //Notify all OTHER clients that user joined the game
            String message = username + " joined the game";
            NotificationMessage notification = new NotificationMessage(message);
            connections.broadcast(username, notification);
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
        //Make sure move is valid-- Call makeMove in Phase 1 code
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame game = gameData.game();
        try {
            game.makeMove(move);
        } catch (InvalidMoveException e) {
            throw new DataAccessException(e.getMessage());
        }
        //Update ChessGame

        //Send LoadGameMessage to ALL clients

        //Send NotificationMessage to all OTHER clients spelling out what move was made
        String message = String.format("%s made a move: %s to %s", username, startPos, endPos);

        //Notify ALL clients if in check/checkmate/stalemate

    }

    public void leaveGame(Session session, String username, UserGameCommand command) throws DataAccessException {
        try {
            //Remove user from ChessGame (if client is observer, do nothing
            Integer gameID = command.getGameID();
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData.whiteUsername().equals(username)) {
                gameDAO.updateGame(gameID, "WHITE", null);
            } else if (gameData.blackUsername().equals(username)) {
                gameDAO.updateGame(gameID, "BLACK", null);
            }
            //Remove client's connection from ConnectionManager
            connections.remove(username);

            //Notify other clients that user left the game
            String message = username + " left the game";
            NotificationMessage notification = new NotificationMessage(message);
            connections.broadcast(username, notification);
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void resign(Session session, String username, UserGameCommand command) throws IOException {
        //End ChessGame—do NOT remove the resigning user

        //Notify ALL clients that game has been forfeited
        String message = username + " forfeited the game";
        NotificationMessage notification = new NotificationMessage(message);
        connections.broadcast(username, notification);
    }
}
