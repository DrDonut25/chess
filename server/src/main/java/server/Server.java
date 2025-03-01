package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import requestsresults.RegisterRequest;
import requestsresults.RegisterResult;
import service.GameService;
import service.UserService;
import service.UserServiceException;
import spark.*;

public class Server {
    private UserService userService;
    private GameService gameService;

    public void setServices(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.userService = new UserService(authDAO, userDAO);
        this.gameService = new GameService(gameDAO);
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
        if (registerResult.message() != null) {
            if (registerResult.message().equals("Error: already taken")) {
                response.status(403);
            } else if (registerResult.message().equals("Error: Data Access Exception")) {
                response.status(500);
            }
        } else {
            response.status(200);
        }
        return serializer.toJson(registerResult);
    }

    private Response clear(Request request, Response response) {
        userService.clear();
        gameService.clearGames();
        return response;
    }
}
