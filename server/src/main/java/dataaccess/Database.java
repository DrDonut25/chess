package dataaccess;

import model.UserData;
import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private Map<String, UserData> users;
    private Map<String, AuthData> auths;
    private Map<Integer, GameData> games;

    public Database() {
        users = new HashMap<String, UserData>();
        auths = new HashMap<String, AuthData>();
        games = new HashMap<Integer, GameData>();
    }

    public Map<String, UserData> getUsers() {
        return users;
    }

    public void addUser(UserData user) {
        users.put(user.username(), user);
    }

    public void clearUsers() {
        users = new HashMap<String, UserData>();
    }

    public Map<String, AuthData> getAuths() {
        return auths;
    }

    public void addAuth(AuthData auth) {
        auths.put(auth.authToken(), auth);
    }

    public void clearAuths() {
        auths = new HashMap<String, AuthData>();
    }

    public Map<Integer, GameData> getGames() {
        return games;
    }

    public void addGame(GameData game) {
        games.put(game.gameID(), game);
    }

    public void clearGames() {
        games = new HashMap<Integer, GameData>();
    }
}
