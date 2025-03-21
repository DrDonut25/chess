package ui;

public class GameClient implements Client {
    private final String serverURL;
    private final Repl repl;

    public GameClient(String serverURL, Repl repl) {
        this.serverURL = serverURL;
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
