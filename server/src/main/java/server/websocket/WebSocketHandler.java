package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
    private ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        UserGameCommand gameCommand = new Gson().fromJson(msg, UserGameCommand.class);
        String username = getUsername(gameCommand.getAuthToken());

        switch (gameCommand.getCommandType()) {
            case CONNECT -> connect(session, username, gameCommand);
            case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) gameCommand);
            case LEAVE -> leaveGame(session, username, gameCommand);
            case RESIGN -> resign(session, username, gameCommand);
        }
    }

    public String getUsername(String authToken) {
        return "";
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
