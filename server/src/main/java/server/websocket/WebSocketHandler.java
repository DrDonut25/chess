package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
    private ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        try {
            UserGameCommand gameCommand = new Gson().fromJson(msg, UserGameCommand.class);

            String username = getUsername(gameCommand.getAuthToken());
        } catch (Exception e) {
            connections.sendMessage();
        }
    }
}
