package account.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "You must login!")
public class NotLoggedInException extends RuntimeException {
    public NotLoggedInException() {
        super();
    }
}
