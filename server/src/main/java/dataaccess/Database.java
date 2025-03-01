package dataaccess;

import model.UserData;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public class Database {
    private ArrayList<UserData> users;
    private ArrayList<AuthData> auths;
    private ArrayList<GameData> games;

    public Database() {
        users = new ArrayList<>();
        auths = new ArrayList<>();
        games = new ArrayList<>();
    }

    public ArrayList<UserData> getUsers() {
        return users;
    }

    public void addUser(UserData user) {
        users.add(user);
    }

    public ArrayList<AuthData> getAuths() {
        return auths;
    }

    public void addAuth(AuthData auth) {
        auths.add(auth);
    }

    public ArrayList<GameData> getGames() {
        return games;
    }

    public void addGame(GameData game) {
        games.add(game);
    }
}
