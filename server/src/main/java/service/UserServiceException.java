package service;

public class UserServiceException extends RuntimeException {
    private final int statusCode;

    public UserServiceException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
