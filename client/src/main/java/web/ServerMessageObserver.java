package web;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public interface ServerMessageObserver {
    void notify(ServerMessage message);
    void displayNotification(NotificationMessage notification);
    void displayError(ErrorMessage errorMessage);
    void loadGame(LoadGameMessage gameMessage);
}
