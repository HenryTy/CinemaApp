package ty.henry.cinemaapp.error;

public class UserDoesntExistException extends RuntimeException {

    public UserDoesntExistException() {}

    public UserDoesntExistException(String message) {
        super(message);
    }

}
