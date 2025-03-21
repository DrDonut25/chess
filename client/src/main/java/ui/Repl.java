package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private Client client;

    public Repl(String serverURL) {
        client = new LoginClient(serverURL, this);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                System.out.println(e.toString());
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + ERASE_SCREEN + ">>>" + SET_TEXT_COLOR_GREEN);
    }
}
