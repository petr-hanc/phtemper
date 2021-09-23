package phtemper.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** REST API - handle errors */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	/** Send HTTP status BAD_REQUEST and error message from exception ex */
	private ResponseEntity<ErrorResponse> respondBadRequest(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		
		error.setStatus(HttpStatus.BAD_REQUEST.value());  //NOT_FOUND
		error.setMessage(ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		
	}
	
	/** Handle deletion of non-existent id error */
	@ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleException(EmptyResultDataAccessException ex) {
		return respondBadRequest(ex);
	}
	
	/** Handle other exceptions */
	@ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
		return respondBadRequest(ex);
	}
}
