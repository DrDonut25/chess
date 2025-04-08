package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

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
    public void onMessage(Session session, String msg) {
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
            connections.sendMessage(new ErrorMessage("Error: Unauthorized"));
        }
    }

    private String getUsername(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken).username();
    }

    public void connect(Session session, String username, UserGameCommand command) {

    }

    public void makeMove(Session session, String username, MakeMoveCommand command) {

    }

    public void leaveGame(Session session, String username, UserGameCommand command) {

    }

    public void resign(Session session, String username, UserGameCommand command) {

    }
}
