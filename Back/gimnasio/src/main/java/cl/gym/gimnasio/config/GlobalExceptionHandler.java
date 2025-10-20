package cl.gym.gimnasio.config;

import cl.gym.gimnasio.dto.response.GeminiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeminiResponse> handleException(Exception e) {
        GeminiResponse response = new GeminiResponse(
                "Error en el servicio de IA: " + e.getMessage(),
                "error"
        );
        return ResponseEntity.internalServerError().body(response);
    }
}