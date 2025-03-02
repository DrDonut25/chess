package server;

import com.google.gson.Gson;
import dataaccess.*;
import requestsresults.*;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private final UserService userService;
    private final GameService gameService;

    public Server() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        this.userService = new UserService(authDAO, userDAO);
        this.gameService = new GameService(authDAO, gameDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
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

    private String logout(Request request, Response response) {
        var serializer = new Gson();
        String authToken = serializer.fromJson(request.headers("authorization"), String.class);
        LogoutResult logoutResult = userService.logout(authToken);
        if (logoutResult.message() != null) {
            switch (logoutResult.message()){
                case "Error: unauthorized" -> response.status(401);
                case "Error: Data Access Exception" -> response.status(500);
            }
        } else {
            response.status(200);
        }
        return serializer.toJson(logoutResult);
    }

    private String listGames(Request request, Response response) {
        var serializer = new Gson();
        String authToken = serializer.fromJson(request.headers("authorization"), String.class);
        ListGameResult listGameResult = gameService.listGames(authToken);
        if (listGameResult.message() != null) {
            switch (listGameResult.message()) {
                case "Error: unauthorized" -> response.status(401);
                case "Error: Data Access Exception" -> response.status(500);
            }
        } else {
            response.status(200);
        }
        return serializer.toJson(listGameResult);
    }

    private String createGame(Request request, Response response) {
        var serializer = new Gson();
        String authToken = serializer.fromJson(request.headers("authorization"), String.class);
        IntermediateCreateRequest interReq = serializer.fromJson(request.body(), IntermediateCreateRequest.class);
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, interReq.gameName());
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        if (createGameResult.message() != null) {
            switch (createGameResult.message()) {
                case "Error: bad request" -> response.status(400);
                case "Error: unauthorized" -> response.status(401);
                case "Error: Data Access Exception" -> response.status(500);
            }
        } else {
            response.status(200);
        }
        return serializer.toJson(createGameResult);
    }

    private String joinGame(Request request, Response response) {
        var serializer = new Gson();
        String header= request.headers("authorization");
        String authToken = serializer.fromJson(request.headers("authorization"), String.class);
        IntermediateJoinRequest interReq = serializer.fromJson(request.body(), IntermediateJoinRequest.class);
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, interReq.playerColor(), interReq.gameID());
        JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
        if (joinGameResult.message() != null) {
            switch (joinGameResult.message()) {
                case "Error: bad request" -> response.status(400);
                case "Error: unauthorized" -> response.status(401);
                case "Error: already taken" -> response.status(403);
                case "Error: Data Access Exception" -> response.status(500);
            }
        } else {
            response.status(200);
        }
        return serializer.toJson(joinGameResult);
    }
}
