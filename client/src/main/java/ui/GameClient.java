package ui;

public class GameClient implements Client {
    private final String serverUrl;
    private final Repl repl;

    public GameClient(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;
        this.repl = repl;
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
