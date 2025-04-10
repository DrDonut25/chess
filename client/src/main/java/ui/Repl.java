package ui;

import chess.ChessGame;
import exception.DataAccessException;
import model.GameData;
import web.ServerMessageObserver;
import websocket.messages.*;

import java.util.Scanner;
import java.util.Stack;

import static ui.EscapeSequences.*;

public class Repl implements ServerMessageObserver {
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
                clientStack.push(new PostLoginClient(serverUrl, client.getAuthToken(), this));
            }
        } else if (client instanceof PostLoginClient) {
            if (result.startsWith("Logged out")) {
                clientStack.pop();
            } else if (result.startsWith("Joined")) {
                GameData game = null;
                clientStack.push(new GameClient(serverUrl, client.getAuthToken(), game, this, false));
            } else if (result.startsWith("Observing")) {
                GameData game = null;
                clientStack.push(new GameClient(serverUrl, client.getAuthToken(), game, this, true));
            }
        } else if (client instanceof GameClient) {
            if (result.startsWith("Resigned") || result.startsWith("Left")) {
                clientStack.pop();
            }
        }
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification((NotificationMessage) message);
            case ERROR -> displayError((ErrorMessage) message);
            case LOAD_GAME -> loadGame((LoadGameMessage) message);
        }
        printPrompt();
    }

    @Override
    public void displayNotification(NotificationMessage notification) {
        System.out.println(SET_TEXT_COLOR_YELLOW + notification.getMessage());
    }

    @Override
    public void displayError(ErrorMessage errorMessage) {
        System.out.println(SET_TEXT_COLOR_RED + errorMessage.getMessage());
    }

    @Override
    public void loadGame(LoadGameMessage gameMessage) {
        GameData gameData = gameMessage.getGame();
        ChessGame game = gameData.game();
        BoardSketcher.drawBoard(gameMessage.isWhiteOriented(), game);
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
