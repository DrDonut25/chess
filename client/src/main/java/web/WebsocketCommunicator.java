package web;

import com.google.gson.Gson;
import exception.DataAccessException;
import websocket.messages.ServerMessage;

import javax.websocket.ContainerProvider;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

public class WebsocketCommunicator {
    private Session session;
    private ServerMessageObserver messageObserver;

    public WebsocketCommunicator(String url, ServerMessageObserver observer) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.messageObserver = observer;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    messageObserver.notify(serverMessage);
                }
            });
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
