package cl.gym.gimnasio.dto.response;

import java.time.LocalDateTime;

public class GeminiResponse {
    private String response;
    private String model;
    private LocalDateTime timestamp;
    private String usage; // Información de uso (opcional)

    // Constructores
    public GeminiResponse() {}

    public GeminiResponse(String response, String model) {
        this.response = response;
        this.model = model;
        this.timestamp = LocalDateTime.now();
    }

    public GeminiResponse(String response, String model, String usage) {
        this.response = response;
        this.model = model;
        this.usage = usage;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getUsage() { return usage; }
    public void setUsage(String usage) { this.usage = usage; }
}