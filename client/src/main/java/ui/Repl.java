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
        this.client = client;
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
                if (client instanceof LoginClient && (result.equals("login") || result.equals("register"))) {
                    changeClient(new PostLoginClient(serverURL, this));
                    this.run();
                } else if (client instanceof PostLoginClient && (result.equals("join") || result.equals("observe"))) {
                    changeClient(new GameClient(serverURL, this));
                    this.run();
                }
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
