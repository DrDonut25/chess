package ui;

import chess.ChessGame;
import exception.DataAccessException;
import model.GameData;
import requestsresults.CreateGameRequest;
import requestsresults.JoinGameRequest;
import requestsresults.ListGameResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

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

    public String logout() throws DataAccessException {
        server.logout();
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
        ListGameResult listGameResult = server.listGames();
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
            Integer gameID = Integer.valueOf(params[0]);
            String playerColor = params[1];
            server.joinGame(new JoinGameRequest(authToken, playerColor, gameID));
            var result = new StringBuilder();
            result.append(String.format("Joined game %s as team %s", gameID, playerColor));
            result.append("\n");
            if (playerColor.equals("BLACK")) {
                result.append(drawBoard(false, getGame(gameID)));
            } else {
                result.append(drawBoard(true, getGame(gameID)));
            }
            return result.toString();
        } else {
            throw new DataAccessException("Error: invalid number of arguments — expected <ID> <WHITE|BLACK>");
        }
    }

    public String observe(String[] params) throws DataAccessException {
        if (params.length == 1) {
            Integer gameID = Integer.valueOf(params[0]);
            return drawBoard(true, getGame(gameID));
        } else {
            throw new DataAccessException("Error: invalid number of arguments — expected <ID>");
        }
    }

    private ChessGame getGame(Integer gameID) throws DataAccessException {
        ListGameResult listGameResult = server.listGames();
        Collection<GameData> games = listGameResult.games();
        for (GameData game: games) {
            if (Objects.equals(game.gameID(), gameID)) {
                return game.game();
            }
        }
        throw new DataAccessException("Error: invalid game ID");
    }

    /*
      a b c d e f g h
    8 r n b q k b n r 8
    7 p p p p p p p p 7
    6                 6
    5                 5
    4                 4
    3                 3
    2 P P P P P P P P 2
    1 R N B Q K B N R 1
      a b c d e f g h
     */
    private String drawBoard(boolean isWhiteOriented, ChessGame game) {
        StringBuilder sb = new StringBuilder();
        sb.append("a b c d e f g h\n");
        if (isWhiteOriented) {
            for (int i = 8; i > 0; i--) {
                sb.append(i);
                //Read ChessGame contents, printing relevant pieces on current row
                sb.append(i).append("\n");
            }
        } else {
            for (int i = 1; i <= 8; i++) {
                sb.append(i);
                //Read ChessGame contents, printing relevant pieces on current row
                sb.append(i).append("\n");
            }
        }
        sb.append("a b c d e f g h");
        return sb.toString();
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
