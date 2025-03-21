import chess.*;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        var serverURL = "http://localhost:8080";
        System.out.println("Welcome to CS240 chess. Type \"help\" to get started.");
        new Repl(serverURL).run();
    }
}