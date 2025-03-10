import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        try {
            var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            System.out.println("♕ 240 Chess Server: " + piece);
            Server server = new Server();
            server.setSQLDAO();
            server.run(8080);
        } catch (Throwable e) {
            System.out.printf("Server failed to start: %s%n", e.getMessage());
        }
    }
}