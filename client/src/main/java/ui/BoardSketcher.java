package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardSketcher {
    public static String drawBoard(boolean isWhiteOriented, ChessGame game) {
        ChessBoard board = game.getBoard();
        StringBuilder sb = new StringBuilder();
        String resetColors = EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR;
        String newLine = resetColors + "\n";
        String labelFont = EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_BLACK;
        if (isWhiteOriented) {
            String columnText = " a\u2003 b\u2003 c\u2003 d\u2003 e\u2003 f\u2003 g\u2003 h";
            String columnLabels = labelFont + EscapeSequences.EMPTY + columnText + "    ";
            sb.append(columnLabels).append(newLine);
            for (int row = 8; row > 0; row--) {
                sb.append(labelFont).append(" ").append(String.format("%d ", row));
                //Read ChessGame contents, printing relevant pieces on current row
                for (int col = 1; col <= 8; col++) {
                    if (isDarkTile(row, col)) {
                        sb.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                    } else {
                        sb.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    }
                    printPiece(sb, board, row, col);
                }
                sb.append(labelFont).append(" ").append(String.format("%d ", row));
                sb.append(newLine);
            }
            sb.append(columnLabels).append(resetColors).append(newLine);
        } else {
            String columnText = " h\u2003 g\u2003 f\u2003 e\u2003 d\u2003 b\u2003 c\u2003 a";
            String columnLabels = labelFont + EscapeSequences.EMPTY + columnText + "    ";
            sb.append(columnLabels).append(newLine);
            for (int row = 1; row <= 8; row++) {
                sb.append(labelFont).append(" ").append(String.format("%d ", row));
                //Read ChessGame contents, printing relevant pieces on current row
                for (int col = 1; col <= 8; col++) {
                    if (isDarkTile(row, 9 - col)) {
                        sb.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                    } else {
                        sb.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    }
                    printPiece(sb, board, row, 9 - col);
                }
                sb.append(labelFont).append(" ").append(String.format("%d ", row));
                sb.append(newLine);
            }
            sb.append(columnLabels).append(resetColors).append(newLine);
        }
        return sb.toString();
    }

    public static String drawLegalMoves(boolean isWhiteOriented, ChessGame game, ChessPosition position) {
        return "";
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
