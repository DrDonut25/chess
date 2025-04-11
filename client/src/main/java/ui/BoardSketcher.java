package ui;

import chess.*;

import java.util.Collection;
import java.util.Iterator;

public class BoardSketcher {
    public static String drawBoard(boolean isWhiteOriented, ChessGame game, Collection<ChessMove> legalMoves) {
        ChessBoard board = game.getBoard();
        StringBuilder sb = new StringBuilder();
        String resetColors = EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR;
        String newLine = resetColors + "\n";
        String labelFont = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK;
        if (isWhiteOriented) {
            //Add top column label
            String columnText = " a\u2003 b\u2003 c\u2003 d\u2003 e\u2003 f\u2003 g\u2003 h";
            String columnLabels = labelFont + EscapeSequences.EMPTY + columnText + "    ";
            sb.append(columnLabels).append(newLine);
            //Add ChessGame contents (with row labels on either side)
            for (int row = 8; row > 0; row--) {
                //Add left row label
                sb.append(labelFont).append(" ").append(String.format("%d ", row));
                for (int col = 1; col <= 8; col++) {
                    //Set appropriate tile color, highlighting legal move tiles (if any)
                    setTileColor(sb, row, col, legalMoves);
                    //Add ChessPiece
                    printPiece(sb, board, row, col);
                }
                //Add right row label
                sb.append(labelFont).append(" ").append(String.format("%d ", row));
                sb.append(newLine);
            }
            //Add bottom column label
            sb.append(columnLabels).append(resetColors).append(newLine);
        } else {
            //Add top column label
            String columnText = " h\u2003 g\u2003 f\u2003 e\u2003 d\u2003 b\u2003 c\u2003 a";
            String columnLabels = labelFont + EscapeSequences.EMPTY + columnText + "    ";
            sb.append(columnLabels).append(newLine);
            //Add rows
            for (int row = 1; row <= 8; row++) {
                //Add left row label
                sb.append(labelFont).append(" ").append(String.format("%d ", row));
                for (int col = 1; col <= 8; col++) {
                    //Set tile color
                    setTileColor(sb, row, col, legalMoves);
                    //Print tile contents
                    printPiece(sb, board, row, 9 - col);
                }
                //Add right row label
                sb.append(labelFont).append(" ").append(String.format("%d ", row));
                sb.append(newLine);
            }
            //Add bottom column label
            sb.append(columnLabels).append(resetColors).append(newLine);
        }
        return sb.toString();
    }

    private static void setTileColor(StringBuilder sb, int row, int col, Collection<ChessMove> legalMoves) {
        ChessPosition position = new ChessPosition(row, col);
        if (isDarkTile(row, col)) {
            if (legalMoves != null && isStartPos(position, legalMoves)) {
                sb.append(EscapeSequences.SET_BG_COLOR_YELLOW);
            } else if (legalMoves != null && isLegalPos(position, legalMoves)) {
                sb.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
            } else {
                sb.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            }
        } else {
            if (legalMoves != null && isStartPos(position, legalMoves)) {
                sb.append(EscapeSequences.SET_BG_COLOR_YELLOW);
            } else if (legalMoves != null && isLegalPos(position, legalMoves)) {
                sb.append(EscapeSequences.SET_BG_COLOR_GREEN);
            } else {
                sb.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            }
        }
    }

    //For highlighting legal moves
    private static boolean isStartPos(ChessPosition position, Collection<ChessMove> legalMoves) {
        Iterator<ChessMove> iterator = legalMoves.iterator();
        ChessMove move = iterator.next();
        return move.getStartPosition().equals(position);
    }

    //For highlighting legal moves
    private static boolean isLegalPos(ChessPosition position, Collection<ChessMove> legalMoves) {
        for (ChessMove move: legalMoves) {
            if (move.getEndPosition().equals(position)) {
                //Consider removing legalMove to improve runtime?
                return true;
            }
        }
        return false;
    }

    private static void printPiece(StringBuilder sb, ChessBoard board, int row, int col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            sb.append(EscapeSequences.EMPTY);
        } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            sb.append(EscapeSequences.SET_TEXT_COLOR_BLACK);
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                sb.append(EscapeSequences.BLACK_PAWN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                sb.append(EscapeSequences.BLACK_ROOK);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                sb.append(EscapeSequences.BLACK_KNIGHT);
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                sb.append(EscapeSequences.BLACK_BISHOP);
            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                sb.append(EscapeSequences.BLACK_QUEEN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                sb.append(EscapeSequences.BLACK_KING);
            }
        } else {
            sb.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                sb.append(EscapeSequences.WHITE_PAWN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                sb.append(EscapeSequences.WHITE_ROOK);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                sb.append(EscapeSequences.WHITE_KNIGHT);
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                sb.append(EscapeSequences.WHITE_BISHOP);
            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                sb.append(EscapeSequences.WHITE_QUEEN);
            } else {
                sb.append(EscapeSequences.WHITE_KING);
            }
        }
    }

    private static boolean isDarkTile(int row, int col) {
        if (row % 2 == 1) {
            return col % 2 == 1;
        } else {
            return col % 2 == 0;
        }
    }
}
