package com.springboot.filemanipulation.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalException {
	@ExceptionHandler(ResourceNotFoundException.class)
	  public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
	    ErrorMessage message = new ErrorMessage(
	        HttpStatus.NOT_FOUND.value(),
	        new Date(),
	        ex.getMessage(),
	        request.getDescription(false));
	    
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
	  }
	@ExceptionHandler(BadRequestException.class)
	  public ResponseEntity<ErrorMessage> invalidRequest(BadRequestException ex, WebRequest request) {
	    ErrorMessage message = new ErrorMessage(
	        HttpStatus.BAD_REQUEST.value(),
	        new Date(),
	        ex.getMessage(),
	        request.getDescription(false));
	    
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	  }
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	  public ResponseEntity<ErrorMessage> invalidValidationRequest(MethodArgumentTypeMismatchException ex, WebRequest request) {
	    ErrorMessage message = new ErrorMessage(
	        HttpStatus.BAD_REQUEST.value(),
	        new Date(),
	        ex.getMessage(),
	        request.getDescription(false));
	    
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	  }
	  
	  @ExceptionHandler(MaxUploadSizeExceededException.class)
	    public ResponseEntity<ErrorMessage> handleMaxSizeException(MaxUploadSizeExceededException exc) {
		  ErrorMessage message = new ErrorMessage(
			        HttpStatus.EXPECTATION_FAILED.value(),
			        new Date(),
			        "\"Unable to upload. File is too large!\"",
			        exc.getMessage());
		  return new ResponseEntity<ErrorMessage>(message, HttpStatus.EXPECTATION_FAILED);
	    }
	  @ExceptionHandler(NoHandlerFoundException.class)
	    public ResponseEntity<ErrorMessage> handleError404(NoHandlerFoundException ex, WebRequest request) {
		  ErrorMessage message = new ErrorMessage(
			        HttpStatus.NOT_FOUND.value(),
			        new Date(),
			        ex.getMessage(),
			        request.getDescription(true));
		  return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
	    }
	  
	  @ExceptionHandler(Exception.class)
	  public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
	    ErrorMessage message = new ErrorMessage(
	        HttpStatus.INTERNAL_SERVER_ERROR.value(),
	        new Date(),
	        ex.getMessage(),
	        request.getDescription(false));
	    
	    return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	  }

}
