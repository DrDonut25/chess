package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements pieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece pawn = board.getPiece(position);
        ArrayList<ChessMove> moves = new ArrayList<>();
        //pawn can only move forward 1 space unless it's on row 2 (white) or row 7 (black), to which add a second move space
        //forward pawn movements
        if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE) {
            //check if pawn hasn't moved yet (impossible to promote pawn from starting position
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
                ChessMove knightPromo = new ChessMove(position,endPos, ChessPiece.PieceType.KNIGHT);
                ChessMove bishopPromo = new ChessMove(position,endPos, ChessPiece.PieceType.BISHOP);
                ChessMove rookPromo = new ChessMove(position,endPos, ChessPiece.PieceType.ROOK);
                ChessMove queenPromo = new ChessMove(position,endPos, ChessPiece.PieceType.QUEEN);
                moves.add(knightPromo);
                moves.add(bishopPromo);
                moves.add(rookPromo);
                moves.add(queenPromo);
            }
            //check for diagonal enemy pieces
            endPos = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            if (endPos.isValidPosition() && board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (endPos.getRow() == 8) {
                    ChessMove knightPromo = new ChessMove(position,endPos, ChessPiece.PieceType.KNIGHT);
                    ChessMove bishopPromo = new ChessMove(position,endPos, ChessPiece.PieceType.BISHOP);
                    ChessMove rookPromo = new ChessMove(position,endPos, ChessPiece.PieceType.ROOK);
                    ChessMove queenPromo = new ChessMove(position,endPos, ChessPiece.PieceType.QUEEN);
                    moves.add(knightPromo);
                    moves.add(bishopPromo);
                    moves.add(rookPromo);
                    moves.add(queenPromo);
                } else {
                    ChessMove attackMove = new ChessMove(position, endPos);
                    moves.add(attackMove);
                }
            }
            //check other diagonal
            endPos = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
            if (endPos.isValidPosition() && board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (endPos.getRow() == 8) {
                    ChessMove knightPromo = new ChessMove(position,endPos, ChessPiece.PieceType.KNIGHT);
                    ChessMove bishopPromo = new ChessMove(position,endPos, ChessPiece.PieceType.BISHOP);
                    ChessMove rookPromo = new ChessMove(position,endPos, ChessPiece.PieceType.ROOK);
                    ChessMove queenPromo = new ChessMove(position,endPos, ChessPiece.PieceType.QUEEN);
                    moves.add(knightPromo);
                    moves.add(bishopPromo);
                    moves.add(rookPromo);
                    moves.add(queenPromo);
                } else {
                    ChessMove attackMove = new ChessMove(position, endPos);
                    moves.add(attackMove);
                }
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
                ChessMove knightPromo = new ChessMove(position,endPos, ChessPiece.PieceType.KNIGHT);
                ChessMove bishopPromo = new ChessMove(position,endPos, ChessPiece.PieceType.BISHOP);
                ChessMove rookPromo = new ChessMove(position,endPos, ChessPiece.PieceType.ROOK);
                ChessMove queenPromo = new ChessMove(position,endPos, ChessPiece.PieceType.QUEEN);
                moves.add(knightPromo);
                moves.add(bishopPromo);
                moves.add(rookPromo);
                moves.add(queenPromo);
            }
            //check for diagonal enemy pieces
            endPos = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            if (endPos.isValidPosition() && board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (endPos.getRow() == 1) {
                    ChessMove knightPromo = new ChessMove(position,endPos, ChessPiece.PieceType.KNIGHT);
                    ChessMove bishopPromo = new ChessMove(position,endPos, ChessPiece.PieceType.BISHOP);
                    ChessMove rookPromo = new ChessMove(position,endPos, ChessPiece.PieceType.ROOK);
                    ChessMove queenPromo = new ChessMove(position,endPos, ChessPiece.PieceType.QUEEN);
                    moves.add(knightPromo);
                    moves.add(bishopPromo);
                    moves.add(rookPromo);
                    moves.add(queenPromo);
                } else {
                    ChessMove attackMove = new ChessMove(position, endPos);
                    moves.add(attackMove);
                }
            }
            //check other diagonal
            endPos = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
            if (endPos.isValidPosition() && board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (endPos.getRow() == 1) {
                    ChessMove knightPromo = new ChessMove(position,endPos, ChessPiece.PieceType.KNIGHT);
                    ChessMove bishopPromo = new ChessMove(position,endPos, ChessPiece.PieceType.BISHOP);
                    ChessMove rookPromo = new ChessMove(position,endPos, ChessPiece.PieceType.ROOK);
                    ChessMove queenPromo = new ChessMove(position,endPos, ChessPiece.PieceType.QUEEN);
                    moves.add(knightPromo);
                    moves.add(bishopPromo);
                    moves.add(rookPromo);
                    moves.add(queenPromo);
                } else {
                    ChessMove attackMove = new ChessMove(position, endPos);
                    moves.add(attackMove);
                }
            }
        }
        return moves;
    }
}
