package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "color=" + color +
                ", type=" + type +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        pieceMovesCalculator movesCalculator;
        if (this.getPieceType() == PieceType.KING) {
            movesCalculator = new KingMovesCalculator();
            return movesCalculator.pieceMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.QUEEN) {
            movesCalculator = new QueenMovesCalculator();
            return movesCalculator.pieceMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.KNIGHT) {
            movesCalculator = new KnightMovesCalculator();
            return movesCalculator.pieceMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.PAWN) {
            movesCalculator = new PawnMovesCalculator();
            return movesCalculator.pieceMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.ROOK) {
            movesCalculator = new RookMovesCalculator();
            return movesCalculator.pieceMoves(board, myPosition);
        } else {
            movesCalculator = new BishopMovesCalculator();
            return movesCalculator.pieceMoves(board, myPosition);
        }
    }
}
