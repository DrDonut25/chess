package web;

import com.google.gson.Gson;
import exception.DataAccessException;
import requestsresults.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class HttpCommunicator {
    private String serverUrl;

    public HttpCommunicator(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String path = "/user";
        return this.makeRequest("POST", path, registerRequest, RegisterResult.class, null);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        String path = "/session";
        return this.makeRequest("POST", path, loginRequest, LoginResult.class, null);
    }

    public LogoutResult logout(String authToken) throws DataAccessException {
        String path = "/session";
        return this.makeRequest("DELETE", path, null, LogoutResult.class, authToken);
    }

    public ListGameResult listGames(String authToken) throws DataAccessException {
        String path = "/game";
        return this.makeRequest("GET", path, null, ListGameResult.class, authToken);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        String path = "/game";
        String authToken = createGameRequest.authToken();
        createGameRequest = new CreateGameRequest(null, createGameRequest.gameName());
        return this.makeRequest("POST", path, createGameRequest, CreateGameResult.class, authToken);
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        String path = "/game";
        String authToken = joinGameRequest.authToken();
        joinGameRequest = new JoinGameRequest(null, joinGameRequest.playerColor(), joinGameRequest.gameID());
        return this.makeRequest("PUT", path, joinGameRequest, JoinGameResult.class, authToken);
    }

    public void clear() throws DataAccessException {
        String path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            this.throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String requestData = new Gson().toJson(request);
            try (OutputStream requestBody = http.getOutputStream()) {
                requestBody.write(requestData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        int status = http.getResponseCode();
        if (status / 100 != 2) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    var map = new Gson().fromJson(new InputStreamReader(respErr), HashMap.class);
                    String message = map.get("message").toString();
                    throw new DataAccessException(message);
                }
            }

            throw new DataAccessException("other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
