package br.com.soulmove.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handlerConstraintViolation(
            ConstraintViolationException ex,
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
