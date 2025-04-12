package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, String username, Session session) {
        Connection connection = new Connection(username, gameID, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcast(String excludeUsername, Integer gameID, ServerMessage message) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        for (Connection connection: connections.values()) {
            if (connection.session.isOpen()) {
                if (!connection.username.equals(excludeUsername)) {
                    if (Objects.equals(connection.gameID, gameID)) {
                        connection.send(message.toString());
                    }
                }
            } else {
                removeList.add(connection);
            }
        }

        //Remove idle connections
        for (Connection idleConnection: removeList) {
            connections.remove(idleConnection.username);
        }
    }
}
