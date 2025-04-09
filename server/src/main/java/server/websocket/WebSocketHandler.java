package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.DataAccessException;
import model.GameData;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.imageio.IIOException;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private ConnectionManager connections = new ConnectionManager();
    private AuthDAO authDAO;
    private GameDAO gameDAO;

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
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) gameCommand);
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
            connections.add(username, session);
            String message = username + " joined the game";
            NotificationMessage notification = new NotificationMessage(message);
            connections.broadcast(username, notification);
            //Observer code?

        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void makeMove(Session session, String username, MakeMoveCommand command) {
        String pos1 = "";
        String pos2 = "";
        String message = String.format("%s made a move: %s to %s", username, pos1, pos2);
        //Update ChessGame (maybe move this up to the top?)

        //Notify user if in check
    }

    public void leaveGame(Session session, String username, UserGameCommand command) throws IOException {
        connections.remove(username);
        String message = username + " left the game";
        NotificationMessage notification = new NotificationMessage(message);
        connections.broadcast(username, notification);
        //Remove user from ChessGame
    }

    public void resign(Session session, String username, UserGameCommand command) throws IOException {
        String message = username + " forfeited the game";
        NotificationMessage notification = new NotificationMessage(message);
        connections.broadcast(username, notification);
        //Remove user from ChessGame and end ChessGame

    }
}
