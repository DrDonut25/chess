package web;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.DataAccessException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.ContainerProvider;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

public class WebSocketFacade {
    private final Session session;
    private final ServerMessageObserver messageObserver;

    public WebSocketFacade(String url, ServerMessageObserver observer) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.messageObserver = observer;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    var gson = new Gson();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        NotificationMessage notification = gson.fromJson(message, NotificationMessage.class);
                        messageObserver.notify(notification);
                    } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                        ErrorMessage errorMessage = gson.fromJson(message, ErrorMessage.class);
                        messageObserver.notify(errorMessage);
                    } else {
                        LoadGameMessage loadGameMessage = gson.fromJson(message, LoadGameMessage.class);
                        messageObserver.notify(loadGameMessage);
                    }
                }
            });
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void connect(String authToken, Integer gameID) throws DataAccessException {
        try {
            UserGameCommand connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(connectCommand));
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void makeMove(ChessMove move, String authToken, Integer gameID) throws DataAccessException {
        try {
            MakeMoveCommand moveCommand = new MakeMoveCommand(move, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(moveCommand));
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void leave(String authToken, Integer gameID) throws DataAccessException {
        try {
            UserGameCommand leaveCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(leaveCommand));
            this.session.close();
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) throws DataAccessException {
        try {
            UserGameCommand resignCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(resignCommand));
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
