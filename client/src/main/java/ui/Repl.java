package ui;

import java.util.Scanner;

public class Repl {
    private Client client;

    public Repl(String serverURL) {
        client = new LoginClient(serverURL, this);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
    }
}
