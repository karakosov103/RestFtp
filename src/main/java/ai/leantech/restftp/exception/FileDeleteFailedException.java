package ai.leantech.restftp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="File not deleted")
public class FileDeleteFailedException extends RuntimeException{
}
