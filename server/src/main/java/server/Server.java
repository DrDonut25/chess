package server;

import com.google.gson.Gson;
import dataaccess.Database;
import requestsresults.RegisterRequest;
import requestsresults.RegisterResult;
import service.GameService;
import service.UserService;
import service.UserServiceException;
import spark.*;

public class Server {
    private UserService userService;
    private GameService gameService;

    public void setMemoryDatabase(Database db) {
        this.userService = new UserService(db);
        this.gameService = new GameService(db);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.post("/user", this::register);
        Spark.delete("/db", this::clear);
        //Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String register(Request request, Response response) throws UserServiceException {
        var serializer = new Gson();
        //Where to place 400/500 exceptions? What exactly causes these errors? How do I avoid sending the wrong exception?
        RegisterRequest registerRequest = serializer.fromJson(request.body(), RegisterRequest.class);
        RegisterResult registerResult = userService.register(registerRequest);
        response.status(200);
        return serializer.toJson(registerResult);
    }

    private Response clear(Request request, Response response) {
        userService.clear();
        gameService.clearGames();
        return response;
    }
}
