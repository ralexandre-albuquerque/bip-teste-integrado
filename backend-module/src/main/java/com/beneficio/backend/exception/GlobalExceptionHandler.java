package com.beneficio.backend.exception;

import com.beneficio.backend.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Captura erros de ordenação (ex: passar campo que não existe no Pageable).
     */
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> handlePropertyReference(PropertyReferenceException ex) {
        log.error("Erro de ordenação: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Campo de ordenação inválido: " + ex.getPropertyName(),
                LocalDateTime.now(),
                List.of("A propriedade '" + ex.getPropertyName() + "' não existe na entidade.")
        );
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Captura erros de validação de campos (@Valid).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        log.error("Erro de validação: {}", detalhes);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Dados de entrada inválidos",
                LocalDateTime.now(),
                detalhes
        );
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Captura qualquer outra exceção não tratada.
     * O log.error(..., ex) garante que o StackTrace apareça no console.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Ocorreu uma exceção não tratada: ", ex);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno no servidor. Consulte o suporte ou verifique os logs.",
                LocalDateTime.now(),
                null // Sem detalhes específicos para segurança (não expor stacktrace ao usuário)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}