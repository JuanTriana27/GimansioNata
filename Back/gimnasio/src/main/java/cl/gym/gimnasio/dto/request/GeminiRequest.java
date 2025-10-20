package cl.gym.gimnasio.dto.request;

public class GeminiRequest {
    private String message;
    private String context; // Opcional: para dar contexto específico

    // Constructores
    public GeminiRequest() {}

    public GeminiRequest(String message) {
        this.message = message;
    }

    public GeminiRequest(String message, String context) {
        this.message = message;
        this.context = context;
    }

    // Getters y Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
}