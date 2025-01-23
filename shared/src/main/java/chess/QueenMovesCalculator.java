package chess;

import java.util.Collection;

public class QueenMovesCalculator implements pieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        //queen shares behavior with both bishop and rook --> call bishop/rook pieceMoves methods
        BishopMovesCalculator bishopMoves = new BishopMovesCalculator();
        RookMovesCalculator rookMoves = new RookMovesCalculator();
        Collection<ChessMove> moves = bishopMoves.pieceMoves(board,position);
        moves.addAll(rookMoves.pieceMoves(board, position));
        return moves;
    }
}
