package br.com.soulmove.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnableToFindEntityException.class)
    public ResponseEntity<ErrorResponse> hadleUnableToFindEntity(
            UnableToFindEntityException ex,
            HttpServletRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND; //404

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(BussinesRuleException.class)
    public ResponseEntity<ErrorResponse> handlerBussinesRule(
            BussinesRuleException ex,
            HttpServletRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST; //400

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Regra de negócio violada",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handlerConstraintViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST; // 400

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Regra violada",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }


    public ResponseEntity<ErrorResponse> handlerDuplicateKey(
            DuplicateKeyException ex,
            HttpServletRequest request
    ){
        HttpStatus status = HttpStatus.CONFLICT; // 409

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Campo unico duplicado",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResponse> hadleEmptyResult(
            EmptyResultDataAccessException ex,
            HttpServletRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND; //404

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> hadleMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST; // 400

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Corpo da request invalido",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    public ResponseEntity<ErrorResponse> hadleCannotConnect(
            CannotGetJdbcConnectionException ex,
            HttpServletRequest request){

        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE; // 503

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Erro interno do servidor",
                "Banco de dados indisponivel no momento",
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    // Intercepta qualquer erro inesperado do sistema (evita expor o stacktrace completo) e devolve HTTP 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Erro interno do servidor",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

}
