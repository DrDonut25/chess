package server;

import com.google.gson.Gson;
import dataaccess.*;
import requestsresults.LoginRequest;
import requestsresults.LoginResult;
import requestsresults.RegisterRequest;
import requestsresults.RegisterResult;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private UserService userService;
    private GameService gameService;

    public Server() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        this.userService = new UserService(authDAO, userDAO);
        this.gameService = new GameService(gameDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.exception(DataAccessException.class, this::exceptionHandler);
        //Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(DataAccessException e, Request request, Response response) {
        var serializer = new Gson();
        response.status(500);
        response.body(serializer.toJson(e));
    }

    private Object clear(Request request, Response response) throws DataAccessException {
        userService.clear();
        gameService.clear();
        response.status(200);
        return "";
    }

    private String register(Request request, Response response) {
        var serializer = new Gson();
        RegisterRequest registerRequest = serializer.fromJson(request.body(), RegisterRequest.class);
        RegisterResult registerResult = userService.register(registerRequest);
        if (registerResult.message() != null) {
            switch (registerResult.message()) {
                case "Error: already taken" -> response.status(403);
                case "Error: Data Access Exception" -> response.status(500);
                case "Error: bad request" -> response.status(400);
            }
        } else {
            response.status(200);
        }
        return serializer.toJson(registerResult);
    }

    private String login(Request request, Response response) {
        var serializer = new Gson();
        LoginRequest loginRequest = serializer.fromJson(request.body(), LoginRequest.class);
        LoginResult loginResult = userService.login(loginRequest);
        if (loginResult.message() != null) {
            switch (loginResult.message()) {
                case "Error: unauthorized" -> response.status(401);
                case "Error: Data Access Exception" -> response.status(500);
            }
        } else {
            response.status(200);
        }
        return serializer.toJson(loginResult);
    }
}
