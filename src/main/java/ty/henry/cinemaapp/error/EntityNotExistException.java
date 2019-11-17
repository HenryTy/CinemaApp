package ty.henry.cinemaapp.error;

public class EntityNotExistException extends RuntimeException {

    public EntityNotExistException() {}

    public EntityNotExistException(String message) {
        super(message);
    }

}
