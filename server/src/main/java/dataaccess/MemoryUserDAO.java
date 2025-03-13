package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private Map<String, UserData> users;

    public MemoryUserDAO() {
        users = new HashMap<String, UserData>();
    }

    public void createUser(String username, String password, String email) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        users.put(username, new UserData(username, hashedPassword, email));
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void clear() {
        users = new HashMap<String, UserData>();
    }
}
