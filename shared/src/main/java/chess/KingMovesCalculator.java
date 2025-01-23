package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements pieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece king = board.getPiece(position);
        ArrayList<ChessMove> moves = new ArrayList<>();
        //check a 3x3 area centered around king for open spaces
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                ChessPosition endPos = new ChessPosition(position.getRow() + i, position.getColumn() + j);
                if (endPos.isValidPosition()) {
                    //check if space is either empty or occupied by a piece belonging to other team
                    if (board.getPiece(endPos) == null || board.getPiece(endPos).getTeamColor() != king.getTeamColor()) {
                        ChessMove move = new ChessMove(position, endPos);
                        moves.add(move);
                    }
                }
            }
        }
        return moves;
    }
}
