package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private Client client;
    private final String serverUrl;

    public Repl(String serverUrl) {
        client = new LoginClient(serverUrl, this);
        this.serverUrl = serverUrl;
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
                    changeClient(new PostLoginClient(serverUrl, this));
                    this.run();
                } else if (client instanceof PostLoginClient && (result.equals("join") || result.equals("observe"))) {
                    changeClient(new GameClient(serverUrl, this));
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
        String state = "[LOGGED_OUT]";
        if (client instanceof PostLoginClient) {
            state = "[LOGGED_IN]";
        } else if (client instanceof GameClient) {
            state = "[GAME]";
        }
        System.out.print("\n" + ERASE_SCREEN + state + " >>> " + SET_TEXT_COLOR_GREEN);
    }
}
