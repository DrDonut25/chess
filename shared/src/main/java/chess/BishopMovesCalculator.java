package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece bishop = board.getPiece(position);
        ArrayList<ChessMove> moves = new ArrayList<>();
        //check diagonals --> row/col can only be incremented/decremented at the same time
        //up-right diagonal
        int i = 1;
        ChessPosition endPos = new ChessPosition(position.getRow() + i, position.getColumn() + i);
        while (endPos.isValidPosition()) {
            //empty: add ChessMove & continue loop; occupied: break loop, but add ChessMove if piece belongs to other team
            if (board.getPiece(endPos) == null) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
            } else if (board.getPiece(endPos).getTeamColor() != bishop.getTeamColor()) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
                break;
            } else {
                break;
            }
            i++;
            endPos = new ChessPosition(position.getRow() + i, position.getColumn() + i);
        }
        //down-left diagonal
        i = -1;
        endPos = new ChessPosition(position.getRow() + i, position.getColumn() + i);
        while (endPos.isValidPosition()) {
            //empty space: add ChessMove & continue loop; occupied: break loop, but add ChessMove if piece belongs to other team
            if (board.getPiece(endPos) == null) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
            } else if (board.getPiece(endPos).getTeamColor() != bishop.getTeamColor()) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
                break;
            } else {
                break;
            }
            i--;
            endPos = new ChessPosition(position.getRow() + i, position.getColumn() + i);
        }
        //up-left diagonal
        i = 1;
        int j = -1;
        endPos = new ChessPosition(position.getRow() + i, position.getColumn() + j);
        while (endPos.isValidPosition()) {
            //empty space: add ChessMove & continue loop; occupied: break loop, but add ChessMove if piece belongs to other team
            if (board.getPiece(endPos) == null) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
            } else if (board.getPiece(endPos).getTeamColor() != bishop.getTeamColor()) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
                break;
            } else {
                break;
            }
            i++;
            j--;
            endPos = new ChessPosition(position.getRow() + i, position.getColumn() + j);
        }
        //down-right diagonal
        i = -1;
        j = 1;
        endPos = new ChessPosition(position.getRow() + i, position.getColumn() + j);
        while (endPos.isValidPosition()) {
            //empty space: add ChessMove & continue loop; occupied: break loop, but add ChessMove if piece belongs to other team
            if (board.getPiece(endPos) == null) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
            } else if (board.getPiece(endPos).getTeamColor() != bishop.getTeamColor()) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
                break;
            } else {
                break;
            }
            i--;
            j++;
            endPos = new ChessPosition(position.getRow() + i, position.getColumn() + j);
        }
        return moves;
    }
}
