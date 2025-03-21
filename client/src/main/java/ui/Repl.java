package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private Client client;
    private final String serverURL;

    public Repl(String serverURL) {
        client = new LoginClient(serverURL, this);
        this.serverURL = serverURL;
    }

    public void changeClient(Client client) {
        if (client instanceof LoginClient) {
            client = new LoginClient(serverURL, this);
        } else if (client instanceof PostLoginClient) {
            client = new PostLoginClient(serverURL, this);
        } else if (client instanceof GameClient) {
            client = new GameClient(serverURL, this);
        }
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
                String msg = e.toString();
                System.out.println(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + ERASE_SCREEN + ">>>" + SET_TEXT_COLOR_GREEN);
    }
}
