package com.inplayrs.rest.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	//get log4j handler
	private static final Logger log = LogManager.getLogger(CustomResponseEntityExceptionHandler.class.getName());

	@ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    protected RestError handleException(InvalidStateException ex) {
		log.error("Error code: "+ex.getRestError().getCode() +", message: "+ex.getRestError().getMessage());
        return ex.getRestError();
    }

	@ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    protected RestError handleException(InvalidParameterException ex) {
		log.error("Error code: "+ex.getRestError().getCode() +", message: "+ex.getRestError().getMessage());
        return ex.getRestError();
    }
	
	@ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    protected RestError handleException(DBException ex) {
		log.error("Error code: "+ex.getRestError().getCode() +", message: "+ex.getRestError().getMessage());
        return ex.getRestError();
    }
	
	/*
	 * TODO: Check if this works - don't think it's getting called.
	 * 		 Need a method of catching all uncaught exceptions
	 */
	@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    protected RestError defaultErrorHandler(HttpServletRequest req, Exception ex) {
            
		RestError restError = new RestError(-1, ex.getMessage());
		log.error("Unhandled error, request URL: "+req.getRequestURL()+", message: "+ex.getMessage());
		ex.printStackTrace();
		return restError;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, 
    															  HttpHeaders headers, HttpStatus status, 
    															  WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
        String error;
        for (FieldError fieldError : fieldErrors) {
            error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
            errors.add(error);
        }
        for (ObjectError objectError : globalErrors) {
            error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
            errors.add(error);
        }
        
        RestError restError = new RestError("Method argument was not valid: "+errors.toString());
        log.error("Error code: "+restError.getCode() +", message: "+restError.getMessage()+", "+request.toString());
        return new ResponseEntity(restError, headers, status);
    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, 
    																 HttpHeaders headers, HttpStatus status, 
    																 WebRequest request) {
        String unsupported = "Unsupported content type: " + ex.getContentType();
        //String supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());
        RestError restError = new RestError(unsupported);
        log.error("Error code: "+restError.getCode() +", message: "+restError.getMessage()+", "+request.toString());
        return new ResponseEntity(restError, headers, status);
    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, 
    															  HttpHeaders headers, HttpStatus status, 
    															  WebRequest request) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        RestError restError;
        if (mostSpecificCause != null) {
            String exceptionName = mostSpecificCause.getClass().getName();
            String message = mostSpecificCause.getMessage();
            restError = new RestError(exceptionName+": "+message);
        } else {
            restError = new RestError(ex.getMessage());
        }
        
        log.error("Error code: "+restError.getCode() +", message: "+restError.getMessage()+", "+request.toString());
        return new ResponseEntity(restError, headers, status);
    }
	
	
	
}
