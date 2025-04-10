package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage {
    private GameData game;
    private boolean isWhiteOriented;

    public LoadGameMessage(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public GameData getGame() {
        return game;
    }

    public boolean isWhiteOriented() {
        return isWhiteOriented;
    }
}
