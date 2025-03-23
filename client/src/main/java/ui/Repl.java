package ui;

import java.util.Scanner;
import java.util.Stack;

import static ui.EscapeSequences.*;

public class Repl {
    private Stack<Client> clientStack;
    private final String serverUrl;

    public Repl(String serverUrl) {
        clientStack = new Stack<>();
        clientStack.push(new LoginClient(serverUrl));
        this.serverUrl = serverUrl;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String result = "";
        Client client = clientStack.peek();
        //exit program if "quit" is typed in at any time, otherwise continue running Repl loop
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
                manageClientStack(result);
            } catch (Throwable e) {
                String msg = e.toString();
                System.out.println(msg);
            }
        }
        System.out.println();
    }

    //Switches Repl's client if a certain endpoint is called by pushing/popping to/from the stack
    private void manageClientStack(String result) {
        Client client = clientStack.peek();
        //Order of Clients: Login, PostLogin, Game. Pop when moving left and push when moving right
        if (client instanceof LoginClient) {
            String authToken = client.getAuthToken();
            if (result.startsWith("Logged in") || result.startsWith("Registered")) {
                clientStack.push(new PostLoginClient(serverUrl, authToken));
            }
        } else if (client instanceof PostLoginClient) {
            String authToken = client.getAuthToken();
            if (result.startsWith("join") || result.startsWith("observe")) {
                clientStack.push(new GameClient(serverUrl, authToken));
            } else if (result.startsWith("Logged out")) {
                clientStack.pop();
            }
        } else if (client instanceof GameClient) {
            if (result.startsWith("Left Game")) {
                clientStack.pop();
            }
        }
    }

    private void printPrompt() {
        String state = "[LOGGED_OUT]";
        Client client = clientStack.peek();
        if (client instanceof PostLoginClient) {
            state = "[LOGGED_IN]";
        } else if (client instanceof GameClient) {
            state = "[GAME]";
        }
        System.out.print("\n" + ERASE_SCREEN + state + " >>> " + SET_TEXT_COLOR_GREEN);
    }
}
