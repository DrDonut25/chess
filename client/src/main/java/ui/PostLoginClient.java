package ui;

public class PostLoginClient implements Client {
    private final String serverUrl;
    private final Repl repl;
    private String authToken;

    public PostLoginClient(String serverUrl, Repl repl, String authToken) {
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
                create <NAME> - create new game
                list - list existing games
                join <ID> <WHITE|BLACK> - join game with specified id
                OBSERVE <ID> - observe game with specified id
                logout - log out
                quit - stop playing chess
                help - list possible commands
                """;
    }
}
