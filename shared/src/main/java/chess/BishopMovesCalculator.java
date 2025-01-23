package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BishopMovesCalculator implements pieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        //check diagonals --> row/col can only be incremented/decremented at the same time
        return moves;
    }
}
