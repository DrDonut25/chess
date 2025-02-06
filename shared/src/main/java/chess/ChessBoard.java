package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares;
    public ChessBoard() {
        squares = new ChessPiece[8][8];
    }

    public ChessBoard(ChessBoard board) { //COPY CONSTRUCTOR
        squares = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            squares[i] = Arrays.copyOf(board.squares[i], board.squares[i].length);
        }
    }
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        //add white pieces on row 1
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(1, i);
            if (i == 1 || i == 8) {
                ChessPiece rook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                this.addPiece(position, rook);
            } else if (i == 2 || i == 7) {
                ChessPiece knight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                this.addPiece(position, knight);
            } else if (i == 3 || i == 6) {
                ChessPiece bishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                this.addPiece(position, bishop);
            } else if (i == 4) {
                ChessPiece queen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                this.addPiece(position,queen);
            } else {
                ChessPiece king = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                this.addPiece(position,king);
            }
        }
        //add white pawns
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(2, i);
            ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            this.addPiece(position, pawn);
        }
        //add black pieces on row 8
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(8, i);
            if (i == 1 || i == 8) {
                ChessPiece rook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                this.addPiece(position, rook);
            } else if (i == 2 || i == 7) {
                ChessPiece knight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                this.addPiece(position, knight);
            } else if (i == 3 || i == 6) {
                ChessPiece bishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                this.addPiece(position, bishop);
            } else if (i == 4) {
                ChessPiece queen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                this.addPiece(position,queen);
            } else {
                ChessPiece king = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                this.addPiece(position,king);
            }
        }
        //add black pawns
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(7, i);
            ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            this.addPiece(position, pawn);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessBoard that)) {
            return false;
        }
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ChessPiece[] row: squares) {
            for (ChessPiece piece : row) {
                if (piece == null) {
                    sb.append("0 ");
                } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        sb.append("P ");
                    } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                        sb.append("R ");
                    } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                        sb.append("N ");
                    } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                        sb.append("B ");
                    } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                        sb.append("Q ");
                    } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                        sb.append("K ");
                    }
                } else {
                    if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        sb.append("p ");
                    } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                        sb.append("r ");
                    } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                        sb.append("n ");
                    } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                        sb.append("b ");
                    } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                        sb.append("q ");
                    } else {
                        sb.append("k ");
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
