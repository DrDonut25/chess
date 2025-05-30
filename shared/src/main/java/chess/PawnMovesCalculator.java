package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece pawn = board.getPiece(position);
        ArrayList<ChessMove> moves = new ArrayList<>();
        //pawn can only move forward 1 space unless it's on row 2 (white) or row 7 (black), to which add a second move space
        //forward pawn movements
        if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition endPos = new ChessPosition (position.getRow() + 1, position.getColumn());
            //check space in front of pawn (no need to call isValidPosition() because pawns get promoted at last row)
            //check if pawn can reach last row and get promoted
            if (board.getPiece(endPos) == null && endPos.getRow() != 8) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
                //check if pawn hasn't moved yet
                if (position.getRow() == 2) {
                    endPos = new ChessPosition(position.getRow() + 2, position.getColumn());
                    if (board.getPiece(endPos) == null) {
                        move = new ChessMove(position,endPos);
                        moves.add(move);
                    }
                }
            } else if (board.getPiece(endPos) == null && endPos.getRow() == 8) {
                addPromoMoves(position, endPos, moves);
            }
            //check for diagonal enemy pieces
            endPos = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            if (hasDiagonalEnemy(endPos, board, ChessGame.TeamColor.WHITE)) {
                addDiagonalMoves(position, endPos, moves, ChessGame.TeamColor.WHITE);
            }
            //check other diagonal
            endPos = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
            if (hasDiagonalEnemy(endPos, board, ChessGame.TeamColor.WHITE)) {
                addDiagonalMoves(position, endPos, moves, ChessGame.TeamColor.WHITE);
            }
        } else {
            //black pawn implementation
            //check if pawn hasn't moved yet (impossible to promote pawn from starting position
            //check space in front of pawn (no need to call isValidPosition() because pawns get promoted at last row)
            ChessPosition endPos = new ChessPosition(position.getRow() - 1, position.getColumn());
            //check if pawn can reach last row and get promoted
            if (board.getPiece(endPos) == null && endPos.getRow() != 1) {
                ChessMove move = new ChessMove(position, endPos);
                moves.add(move);
                //check if pawn hasn't moved yet
                if (position.getRow() == 7) {
                    endPos = new ChessPosition(position.getRow() - 2, position.getColumn());
                    if (board.getPiece(endPos) == null) {
                        move = new ChessMove(position,endPos);
                        moves.add(move);
                    }
                }
            } else if (board.getPiece(endPos) == null && endPos.getRow() == 1) {
                addPromoMoves(position, endPos, moves);
            }
            //check for diagonal enemy pieces
            endPos = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            if (hasDiagonalEnemy(endPos, board, ChessGame.TeamColor.BLACK)) {
                addDiagonalMoves(position, endPos, moves, ChessGame.TeamColor.BLACK);
            }
            //check other diagonal
            endPos = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
            if (hasDiagonalEnemy(endPos, board, ChessGame.TeamColor.BLACK)) {
                addDiagonalMoves(position, endPos, moves, ChessGame.TeamColor.BLACK);
            }
        }
        return moves;
    }

    private boolean hasDiagonalEnemy(ChessPosition endPos, ChessBoard board, ChessGame.TeamColor teamColor) {
        return endPos.isValidPosition() && board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != teamColor;
    }

    private void addDiagonalMoves(ChessPosition position, ChessPosition endPos, ArrayList<ChessMove> moves, ChessGame.TeamColor teamColor) {
        int promoRow;
        if (teamColor == ChessGame.TeamColor.WHITE) {
            promoRow = 8;
        } else {
            promoRow = 1;
        }
        if (endPos.getRow() == promoRow) {
            addPromoMoves(position, endPos, moves);
        } else {
            ChessMove attackMove = new ChessMove(position, endPos);
            moves.add(attackMove);
        }
    }

    private void addPromoMoves(ChessPosition position,  ChessPosition endPos, ArrayList<ChessMove> moves) {
        ChessMove knightPromo = new ChessMove(position, endPos, ChessPiece.PieceType.KNIGHT);
        ChessMove bishopPromo = new ChessMove(position, endPos, ChessPiece.PieceType.BISHOP);
        ChessMove rookPromo = new ChessMove(position, endPos, ChessPiece.PieceType.ROOK);
        ChessMove queenPromo = new ChessMove(position, endPos, ChessPiece.PieceType.QUEEN);
        moves.add(knightPromo);
        moves.add(bishopPromo);
        moves.add(rookPromo);
        moves.add(queenPromo);
    }
}
