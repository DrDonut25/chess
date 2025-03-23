package ui;

import exception.DataAccessException;
import requestsresults.LogoutResult;

import java.util.Arrays;

public class PostLoginClient implements Client {
    private final ServerFacade server;
    private String authToken;

    public PostLoginClient(String serverUrl, String authToken) {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    @Override
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
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                default -> help();
            };
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }

    public String createGame(String[] params) {
        return "";
    }

    public String listGames() {
        return "";
    }

    public String join(String[] params) {
        return "";
    }

    public String observe(String[] params) {
        return "";
    }

    public String logout() throws DataAccessException {
        server.logout();
        authToken = null;
        return "Logged out successfully";
    }

    @Override
    public String help() {
        return """
                create <NAME> - create new game
                list - list existing games
                join <ID> <WHITE|BLACK> - join game with specified id
                observe <ID> - observe game with specified id
                logout - log out
                quit - exit program
                help - list possible commands
                """;
    }
}
