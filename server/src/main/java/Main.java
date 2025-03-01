import chess.*;
import dataaccess.Database;
import server.Server;
import service.GameService;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Database db = new Database();
        Server server = new Server();
        server.setMemoryDatabase(db);
        server.run(8080);
    }
}