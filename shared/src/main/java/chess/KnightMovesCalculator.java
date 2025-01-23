package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KnightMovesCalculator implements pieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece knight = board.getPiece(position);
        ArrayList<ChessMove> moves = new ArrayList<>();
        //knight can only move two spaces in one direction followed by one space in the next --> use arrays for i/j changes, then iterate through both
        int[] iRange = {2,2,-2,-2,1,-1,1,-1};
        int[] jRange = {1,-1,1,-1,2,2,-2,-2};
        for (int k = 0; k < 8; k++) {
            ChessPosition endPos = new ChessPosition(position.getRow() + iRange[k], position.getColumn() + jRange[k]);
            if (endPos.isValidPosition()) {
                if (board.getPiece(endPos) == null || board.getPiece(endPos).getTeamColor() != knight.getTeamColor()) {
                    ChessMove move = new ChessMove(position, endPos);
                    moves.add(move);
                }
            }
        }
        return moves;
    }
}
