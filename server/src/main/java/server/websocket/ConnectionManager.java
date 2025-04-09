package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public Connection getConnection(String username) {
        return connections.get(username);
    }

    public void add(String username, Session session) {
        Connection connection  = new Connection(username, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void sendMessage(ServerMessage message) {

    }

    public void broadcast(String excludeUsername, ServerMessage message) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        for (Connection connection: connections.values()) {
            if (connection.session.isOpen()) {
                if (!connection.username.equals(excludeUsername)) {
                    connection.send(message.toString());
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
