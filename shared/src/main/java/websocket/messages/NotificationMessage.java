package websocket.messages;

public class NotificationMessage extends ServerMessage {
    private String message;

    public NotificationMessage(ServerMessageType type) {
        super(type);
    }
}
