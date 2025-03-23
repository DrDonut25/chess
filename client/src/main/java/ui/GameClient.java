package ui;

public class GameClient implements Client {
    private final String serverUrl;
    private final Repl repl;
    private String authToken;

    public GameClient(String serverUrl, Repl repl, String authToken) {
        this.serverUrl = serverUrl;
        this.repl = repl;
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
                show - draw current game board
                help - list possible commands
                """;
    }
}
