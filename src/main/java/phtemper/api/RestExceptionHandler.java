package phtemper.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	private ResponseEntity<ErrorResponse> respondBadRequest(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		
		error.setStatus(HttpStatus.BAD_REQUEST.value());  //NOT_FOUND
		error.setMessage(ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		
	}
	
	/** Deletion of non-existent id */
	@ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleException(EmptyResultDataAccessException ex) {
		return respondBadRequest(ex);
	}
	
	@ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
		return respondBadRequest(ex);
	}
}
