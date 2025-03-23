package ui;

public class GameClient implements Client {
    private final ServerFacade server;
    private String authToken;

    public GameClient(String serverUrl, String authToken) {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    @Override
    public String getAuthToken() {
        return authToken;
    }

    @Override
    public String eval(String input) {
        return "";
    }

    @Override
    public String help() {
        return """
                redraw - draw current game board
                leave - leave current game
                make_move <START_POSITION> <END_POSITION> - move chess piece
                resign - forfeit game (will not leave game)
                legal_moves <POSITION> - highlights legal moves at specified position
                quit - exit program
                help - list possible commands
                """;
    }
}
