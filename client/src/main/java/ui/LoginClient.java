package ui;

import exception.DataAccessException;

import java.util.Arrays;

public class LoginClient implements Client {
    private final String serverUrl;
    private final Repl repl;
    private String authToken;

    public LoginClient(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    @Override
    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                default -> help();
            };
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }

    public String register(String[] params) throws DataAccessException {
        return "";
    }

    public String login(String[] params) throws DataAccessException {
        return "";
    }

    public String logout() throws DataAccessException {
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
