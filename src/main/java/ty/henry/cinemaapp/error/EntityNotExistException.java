package ty.henry.cinemaapp.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotExistException extends RuntimeException {

    public EntityNotExistException() {}

    public EntityNotExistException(String message) {
        super(message);
    }

}
