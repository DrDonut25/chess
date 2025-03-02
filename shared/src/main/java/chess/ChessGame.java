package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    public ChessGame(ChessGame game) {
        this.teamTurn = game.teamTurn;
        board = new ChessBoard(game.board);
    }
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        ArrayList<ChessMove> valMoves = new ArrayList<>();
        //Check if team is in check. If in check, see if current piece can protect the king
        //check if the piece moving will expose the king to check
        for (ChessMove move: moves) {
            ChessGame simulation = new ChessGame(this);
            ChessBoard simBoard = simulation.getBoard();
            simBoard.addPiece(move.getEndPosition(), simBoard.getPiece(move.getStartPosition()));
            simBoard.addPiece(move.getStartPosition(), null);
            if (!simulation.isInCheck(piece.getTeamColor())) {
                valMoves.add(move);
            }
        }
        return valMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> moves = this.validMoves(move.getStartPosition());
        //first check if piece color matches teamTurn's color --> throw exception otherwise
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece != null && piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("It is not that piece's turn to move!");
        }
        //check if move is valid for piece at starting position and if move's starting position has a piece, otherwise throw InvalidMoveException
        if (piece != null && moves.contains(move)) {
            //update board to reflect move: set start position to null and end position to piece, then switch teamTurn
            //check if move has promotion piece
            if (move.getPromotionPiece() == null) {
                board.addPiece(move.getEndPosition(), piece);
                board.addPiece(move.getStartPosition(), null);
            } else {
                ChessPiece promoPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), promoPiece);
                board.addPiece(move.getStartPosition(), null);
            }
            //switch teamTurn
            if (teamTurn == TeamColor.WHITE) {
                this.setTeamTurn(TeamColor.BLACK);
            } else {
                this.setTeamTurn(TeamColor.WHITE);
            }
        } else {
            throw new InvalidMoveException("Move is invalid");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //iterate through every piece on enemy team, then call pieceMoves on each of them
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece enemyPiece = board.getPiece(position);
                if (enemyThreatensKing(position, enemyPiece, teamColor)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean enemyThreatensKing(ChessPosition position,  ChessPiece enemyPiece, TeamColor teamColor) {
        if (enemyPiece != null && enemyPiece.getTeamColor() != teamColor) {
            //for each enemy piece, check if the king is in their Collection of ChessMoves. If one is found, return true
            Collection<ChessMove> moves = enemyPiece.pieceMoves(board, position);
            for (ChessMove move: moves) {
                ChessPiece piece = board.getPiece(move.getEndPosition());
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!this.isInCheck(teamColor)) {
            return false;
        } else {
            return noValidMoves(teamColor);
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //like checkmate, but is conditional on king not being in check
        if (this.isInCheck(teamColor)) {
            return false;
        } else {
            return noValidMoves(teamColor);
        }
    }

    public boolean noValidMoves(TeamColor teamColor) {
        //iterate through every friendly piece. If all friendly pieces have zero valid moves, return true
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece friendlyPiece = board.getPiece(position);
                if (friendlyPiece != null && friendlyPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> valMoves = this.validMoves(position);
                    if (!valMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", board=" + board.toString() +
                '}';
    }
}
