package com.giuseppetavella.rate_limiter_client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class ErrorsHandler {
    
    
    /**
     * This is the 404 error.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsToSendDTO handleMissingRoute(NoResourceFoundException ex, HttpServletRequest request) {
        String msg = "This resource does not exist, or this endpoint does not exist. "
                        + "Endpoint called: " + request.getMethod() + " " + request.getRequestURI();
        return new ErrorsToSendDTO(msg);
    }

    // @ExceptionHandler(DataIntegrityViolationException.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ErrorsToSendDTO handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    //
    //     LOGGER.error("Error with data integrity violation. DETAILS: {}", ex.getMessage());
    //
    //     problemsEmailService.alertDevIfNonLocal(
    //             "Error with data integrity violation",
    //             ex.getMessage(),
    //             ex
    //     );
    //
    //
    //     String message = ex.getMessage();
    //
    //     if (message.contains("duplicate key")) {
    //         return new ErrorsToSendDTO("Error in database. One or more values already exist and cannot be duplicated.");
    //     }
    //     if (message.contains("foreign key") && message.contains("insert")) {
    //         return new ErrorsToSendDTO("Error in database. One or more referenced resources do not exist.");
    //     }
    //     if (message.contains("foreign key") && message.contains("delete")) {
    //         return new ErrorsToSendDTO("Error in database. This resource cannot be deleted because it is referenced by other data.");
    //     }
    //     if (message.contains("not-null") || message.contains("null value")) {
    //         return new ErrorsToSendDTO("Error in database. One or more required fields are missing.");
    //     }
    //     if (message.contains("check constraint")) {
    //         return new ErrorsToSendDTO("Error in database. One or more values do not meet the required constraints.");
    //     }
    //
    //     return new ErrorsToSendDTO("Error in database. The request contains conflicting or invalid data.");
    // }




    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleMethodArgumentoTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = "The type of some request parameter cannot be cast to its correct type. "
                        +"DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleMethodNotValidMismatch(MethodArgumentNotValidException ex) {
        String msg = "Some fields are missing or are not valid. DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }



    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        String msg = "This media type is not supported. Maybe this request expected another media type? DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }




    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleHTTPMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String msg = "The endpoint does not support this HTTP method. DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }




    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleMaybeMissingBody(HttpMessageNotReadableException ex) {
        String msg = "The request body is malformed or missing. Possible causes: " +
                "the body is missing entirely; " +
                "a field has the wrong type (e.g. a string was given where a number is expected); " +
                "a list was given where a single value is expected, or vice versa; " +
                "an invalid enum value was provided; " +
                "the JSON syntax is invalid (e.g. missing quotes, brackets, or commas); " +
                "a date or number format is incorrect.";

        // i make a list with with the error message coming from the exception
        // i need the specific exception message to give an appropriate message to the client,
        // because this exception seems to prove hard to debug or hard to 
        // trace back the problem
        List<String> errors = List.of(ex.getMessage());

        ErrorsToSendDTO errorsToSend = new ErrorsToSendDTO(msg, errors);

        return errorsToSend;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        String msg = "File size exceeds server file size upload limit.";
        return new ErrorsToSendDTO(msg);
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleRequestIsNotMultipartRequest(MultipartException ex) {
        String msg = "This endpoint expects the request to be multipart form-data, "
                + "but it does not appear to be. Try setting the request headers "
                + "with content type multipart form-data.";
        return new ErrorsToSendDTO(msg);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleRequestIsMissingPart(MissingServletRequestPartException ex) {
        String msg = "This endpoint expects the request to have at least one part in the multipart, "
                + "but it seems there is none. This can happen if you are trying to upload a file. "
                + "Is the endpoint expecting a file upload? "
                + "DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleMissingRequestParameter(MissingServletRequestParameterException ex) {
        String msg = "Some query string parameter is missing in the URL. DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }
    

    // @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ErrorsToSendDTO handleDataAccessApiUsage(InvalidDataAccessApiUsageException ex) {
    //
    //     LOGGER.error("Error in server. DETAILS: {}", ex.getMessage());
    //
    //     problemsEmailService.alertDevIfNonLocal(
    //             "Error in server",
    //             ex.getMessage(),
    //             ex
    //     );
    //
    //     String msg = "Error while using an API. DETAILS: " + ex.getMessage();
    //     return new ErrorsToSendDTO(msg);
    // }

    /**
     * This error occurred when a table did not exist in DB.
     * It said "JDBC exception executing SQL [ERROR: relation "users" does not exist"
     * So it should be a good error handler in cases like this.
     */
    // @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ErrorsToSendDTO handleIncorrectInternalAPIUsage(InvalidDataAccessResourceUsageException ex) {
    //
    //     LOGGER.error("Error in server. DETAILS: {}", ex.getMessage());
    //
    //     problemsEmailService.alertDevIfNonLocal(
    //             "Error in server",
    //             ex.getMessage(),
    //             ex
    //     );
    //
    //     return new ErrorsToSendDTO("There was an error in the server.");
    // }

    // this is a startup error, not an error during request lifecyle.
    // it means, it cannot be caught like i do with other errors
    // @ExceptionHandler(CommandAcceptanceException.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ErrorsToSendDTO handleCommandAcceptanceException(CommandAcceptanceException ex) {
    //     // ex.printStackTrace();
    //     LOGGER.error(ex.getMessage());
    //     return new ErrorsToSendDTO("Fatal error at the ORM level. "
    //                                 +"This is likely due to a fatal error at the database level.");
    // }
    

    // @ExceptionHandler(JSONDeserializationException.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ErrorsToSendDTO handleJSONDeserializationException(JSONDeserializationException ex) {
    //
    //     LOGGER.error("Error while deserializing JSON. DETAILS: {}", ex.getMessage());
    //
    //     problemsEmailService.alertDevIfNonLocal(
    //             "Error while deserializing JSON",
    //             ex.getMessage(),
    //             ex
    //     );
    //
    //     return new ErrorsToSendDTO(ex.getMessage());
    // }


    // @ExceptionHandler(TikaAPIException.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ErrorsToSendDTO handleTikaAPIException(TikaAPIException ex) {
    //
    //     LOGGER.error("Error while using Tika API. DETAILS: {}", ex.getMessage());
    //
    //     problemsEmailService.alertDevIfNonLocal(
    //             "Error while using Tika API",
    //             ex.getMessage(),
    //             ex
    //     );
    //
    //     return new ErrorsToSendDTO(ex.getMessage());
    // }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleGenericException(Exception ex) {
        return new ErrorsToSendDTO("Error in server.");
    }
    
}