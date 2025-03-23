package ui;

import exception.DataAccessException;
import requestsresults.*;

import java.util.Arrays;

public class LoginClient implements Client {
    private final ServerFacade server;
    private String authToken;

    public LoginClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
    }

    public String getAuthToken() {
        return authToken;
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
                default -> help();
            };
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }

    public String register(String[] params) throws DataAccessException {
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterResult regRes = server.register(new RegisterRequest(username, password, email));
            authToken = regRes.authToken();
            return String.format("Registered new user %s and logged in", regRes.username());
        } else {
            throw new DataAccessException("Error: expected <USERNAME> <PASSWORD> <EMAIL>");
        }
    }

    public String login(String[] params) throws DataAccessException {
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            LoginResult loginRes = server.login(new LoginRequest(username, password));
            authToken = loginRes.authToken();
            return String.format("Logged in as %s", loginRes.username());
        } else {
            throw new DataAccessException("Error: expected <USERNAME> <PASSWORD> <EMAIL>");
        }
    }

    @Override
    public String help() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to log in existing user
                quit - exit program
                help - list possible commands
                """;
    }
}
