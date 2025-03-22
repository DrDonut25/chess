package ui;

import exception.DataAccessException;

public class LoginClient implements Client {
    private final String serverURL;
    private final Repl repl;

    public LoginClient(String serverURL, Repl repl) {
        this.serverURL = serverURL;
        this.repl = repl;
    }

    @Override
    public String eval(String input) {
        try {

        } catch (DataAccessException e) {

        }
        return "";
    }

    @Override
    public String help() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to log in existing user
                quit - stop playing chess
                help - list possible commands
                """;
    }
}
