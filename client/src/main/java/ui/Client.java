package ui;

public interface Client {
    String getAuthToken();
    String eval(String input);
    String help();
}
