package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
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
                case "quit" -> "Exiting program";
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

    private String drawBoard(boolean isWhiteOriented, ChessGame game) {
        ChessBoard board = game.getBoard();
        StringBuilder sb = new StringBuilder();
        if (isWhiteOriented) {
            sb.append("a b c d e f g h\n");
            for (int row = 8; row > 0; row--) {
                sb.append(String.format("%d ", row));
                //Read ChessGame contents, printing relevant pieces on current row
                for (int col = 1; col <= 8; col++) {
                    printPiece(sb, board, row, col);
                }
                sb.append(String.format("%d ", row)).append("\n");
            }
            sb.append("a b c d e f g h");
        } else {
            sb.append("h g f e d c b a\n");
            for (int row = 1; row <= 8; row++) {
                sb.append(String.format("%d ", row));
                //Read ChessGame contents, printing relevant pieces on current row
                for (int col = 1; col <= 8; col++) {
                    printPiece(sb, board, 9 - row, 9 - col);
                }
                sb.append(String.format("%d ", row)).append("\n");
            }
            sb.append("h g f e d c b a");
        }
        return sb.toString();
    }

    private void printPiece(StringBuilder sb, ChessBoard board, int row, int col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            sb.append(" ");
        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                sb.append(EscapeSequences.BLACK_PAWN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                sb.append(EscapeSequences.BLACK_ROOK);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                sb.append(EscapeSequences.BLACK_KNIGHT);
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                sb.append(EscapeSequences.BLACK_BISHOP);
            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                sb.append(EscapeSequences.BLACK_QUEEN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                sb.append(EscapeSequences.BLACK_KING);
            }
        } else {
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                sb.append(EscapeSequences.WHITE_PAWN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                sb.append(EscapeSequences.WHITE_ROOK);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                sb.append(EscapeSequences.WHITE_KNIGHT);
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                sb.append(EscapeSequences.WHITE_BISHOP);
            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                sb.append(EscapeSequences.WHITE_QUEEN);
            } else {
                sb.append(EscapeSequences.WHITE_KING);
            }
        }
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
