package ui;

import chess.ChessGame;
import exception.DataAccessException;
import model.GameData;
import requestsresults.CreateGameRequest;
import requestsresults.JoinGameRequest;
import requestsresults.ListGameResult;
import web.ServerFacade;
import web.ServerMessageObserver;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class PostLoginClient implements Client {
    private final ServerFacade server;
    private String authToken;

    public PostLoginClient(String serverUrl, String authToken, ServerMessageObserver messageObserver) {
        server = new ServerFacade(serverUrl, messageObserver);
        this.authToken = authToken;
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
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "quit" -> "Exiting program";
                default -> help();
            };
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }

    public String logout() throws DataAccessException {
        server.logout(authToken);
        authToken = null;
        return "Logged out successfully";
    }

    public String createGame(String[] params) throws DataAccessException {
        if (params.length == 1) {
            String gameName = params[0];
            server.createGame(new CreateGameRequest(authToken, gameName));
            return String.format("Created new game: %s", gameName);
        } else {
            throw new DataAccessException("Error: invalid number of arguments — expected <NAME>");
        }
    }

    public String listGames() throws DataAccessException {
        ListGameResult listGameResult = server.listGames(authToken);
        Collection<GameData> games = listGameResult.games();
        var result = new StringBuilder();
        result.append("Listed Games in format: <ID> -> <GAME_NAME>: <WHITE_USERNAME> vs. <BLACK_USERNAME>\n");
        for (GameData game: games) {
            result.append(game.gameID());
            result.append(" -> ");
            result.append(game.gameName());
            result.append(": ");
            result.append(game.whiteUsername());
            result.append(" vs. ");
            result.append(game.blackUsername());
            result.append("\n");
        }
        return result.toString();
    }

    public String join(String[] params) throws DataAccessException {
        if (params.length == 2) {
            try {
                Integer gameID = Integer.valueOf(params[0]);
                String playerColor = params[1].toUpperCase();
                server.joinGame(new JoinGameRequest(authToken, playerColor, gameID));
                StringBuilder result = new StringBuilder();
                result.append(String.format("Joined game %s as team %s", gameID, playerColor));
                result.append("\n");

                result.append(BoardSketcher.drawBoard(true, getGame(gameID)));
                result.append(BoardSketcher.drawBoard(false, getGame(gameID)));

                return result.toString();
            } catch (NumberFormatException e) {
                throw new DataAccessException("Error: passed ID is not a number");
            }
        } else {
            throw new DataAccessException("Error: invalid number of arguments — expected <ID> <WHITE|BLACK>");
        }
    }

    public String observe(String[] params) throws DataAccessException {
        if (params.length == 1) {
            try {
                Integer gameID = Integer.valueOf(params[0]);
                return BoardSketcher.drawBoard(true, getGame(gameID));
            } catch (NumberFormatException e) {
                throw new DataAccessException("Error: passed ID is not a number");
            }
        } else {
            throw new DataAccessException("Error: invalid number of arguments — expected <ID>");
        }
    }

    private ChessGame getGame(Integer gameID) throws DataAccessException {
        ListGameResult listGameResult = server.listGames(authToken);
        Collection<GameData> games = listGameResult.games();
        for (GameData game: games) {
            if (Objects.equals(game.gameID(), gameID)) {
                return game.game();
            }
        }
        throw new DataAccessException("Error: invalid game ID");
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
