package ui;

public class PostLoginClient implements Client {
    private final String serverURL;
    private final Repl repl;

    public PostLoginClient(String serverURL, Repl repl) {
        this.serverURL = serverURL;
        this.repl = repl;
    }

    @Override
    public String eval(String input) {
        return "";
    }

    @Override
    public String help() {
        return "";
    }
}
