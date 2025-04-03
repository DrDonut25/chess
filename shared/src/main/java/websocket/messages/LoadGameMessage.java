package websocket.messages;

public class LoadGameMessage extends ServerMessage {
    private String game;

    public LoadGameMessage(ServerMessageType type) {
        super(type);
    }
}
