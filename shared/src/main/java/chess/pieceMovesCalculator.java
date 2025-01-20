package chess;

import java.util.Collection;

public interface pieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);
}