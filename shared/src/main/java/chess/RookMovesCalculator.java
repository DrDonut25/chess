package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece rook = board.getPiece(position);
        ArrayList<ChessMove> moves = new ArrayList<>();
        //rook can only move horizontally/vertically --> can only increment/decrement row or column, but never both at the same time
        //right horizontal
        int i = 1;
        ChessPosition endPos = new ChessPosition(position.getRow(), position.getColumn() + i);
        while (endPos.isValidPosition()) {
            //empty: add ChessMove & continue loop; occupied: break loop, but add ChessMove if piece belongs to other team
            if (board.getPiece(endPos) == null) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
            } else if (board.getPiece(endPos).getTeamColor() != rook.getTeamColor()) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
                break;
            } else {
                break;
            }
            i++;
            endPos = new ChessPosition(position.getRow(), position.getColumn() + i);
        }
        //left horizontal
        i = -1;
        endPos = new ChessPosition(position.getRow(), position.getColumn() + i);
        while (endPos.isValidPosition()) {
            //empty: add ChessMove & continue loop; occupied: break loop, but add ChessMove if piece belongs to other team
            if (board.getPiece(endPos) == null) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
            } else if (board.getPiece(endPos).getTeamColor() != rook.getTeamColor()) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
                break;
            } else {
                break;
            }
            i--;
            endPos = new ChessPosition(position.getRow(), position.getColumn() + i);
        }
        //up vertical
        i = 1;
        endPos = new ChessPosition(position.getRow() + i, position.getColumn());
        while (endPos.isValidPosition()) {
            //empty: add ChessMove & continue loop; occupied: break loop, but add ChessMove if piece belongs to other team
            if (board.getPiece(endPos) == null) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
            } else if (board.getPiece(endPos).getTeamColor() != rook.getTeamColor()) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
                break;
            } else {
                break;
            }
            i++;
            endPos = new ChessPosition(position.getRow() + i, position.getColumn());
        }
        //down vertical
        i = -1;
        endPos = new ChessPosition(position.getRow() + i, position.getColumn());
        while (endPos.isValidPosition()) {
            //empty: add ChessMove & continue loop; occupied: break loop, but add ChessMove if piece belongs to other team
            if (board.getPiece(endPos) == null) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
            } else if (board.getPiece(endPos).getTeamColor() != rook.getTeamColor()) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
                break;
            } else {
                break;
            }
            i--;
            endPos = new ChessPosition(position.getRow() + i, position.getColumn());
        }
        return moves;
    }
}
