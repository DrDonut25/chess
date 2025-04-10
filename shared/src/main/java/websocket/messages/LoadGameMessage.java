package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage {
    private final GameData game;
    private final boolean isWhiteOriented;

    public LoadGameMessage(GameData game, boolean isWhiteOriented) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.isWhiteOriented = isWhiteOriented;
    }

    public GameData getGame() {
        return game;
    }

    public boolean isWhiteOriented() {
        return isWhiteOriented;
    }
}
