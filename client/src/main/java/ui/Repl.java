package ui;

import exception.DataAccessException;
import model.GameData;

import java.util.Scanner;
import java.util.Stack;

import static ui.EscapeSequences.*;

public class Repl {
    private final Stack<Client> clientStack;
    private final String serverUrl;

    public Repl(String serverUrl) {
        clientStack = new Stack<>();
        clientStack.push(new LoginClient(serverUrl));
        this.serverUrl = serverUrl;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String result = "";
        //exit program if "quit" is typed in at any time, otherwise continue running Repl loop
        while (!result.equals("quit")) {
            System.out.print(SET_TEXT_COLOR_BLUE);
            Client client = clientStack.peek();
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
                if (result.equals("Exiting program")) {
                    break;
                }
                manageClientStack(result);
            } catch (Throwable e) {
                String msg = e.toString();
                System.out.println(msg);
            }
        }
        System.out.println();
    }

    //Switches Repl's client if a certain endpoint is called by pushing/popping to/from the stack
    private void manageClientStack(String result) throws DataAccessException {
        Client client = clientStack.peek();
        //Order of Clients: Login, PostLogin, Game. Pop when moving left and push when moving right
        if (client instanceof LoginClient) {
            if (result.startsWith("Logged in") || result.startsWith("Registered")) {
                clientStack.push(new PostLoginClient(serverUrl, client.getAuthToken()));
            }
        } else if (client instanceof PostLoginClient) {
            if (result.startsWith("Logged out")) {
                clientStack.pop();
            } else if (result.startsWith("Joined")) {
                GameData game = ((PostLoginClient) client).getGameData();
                if (result.contains("WHITE")) {
                    clientStack.push(new GameClient(serverUrl, client.getAuthToken(), game, false, true));
                } else {
                    clientStack.push(new GameClient(serverUrl, client.getAuthToken(), game, false, false));
                }
            } else if (result.startsWith("Observing")) {
                GameData game = ((PostLoginClient) client).getGameData();
                clientStack.push(new GameClient(serverUrl, client.getAuthToken(), game, true, true));
            }
        } else if (client instanceof GameClient) {
            if (result.startsWith("Left")) {
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
