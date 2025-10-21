package cl.gym.gimnasio.service;

import cl.gym.gimnasio.dto.request.GeminiRequest;
import cl.gym.gimnasio.dto.response.GeminiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key:AIzaSyCtJs8m-_4-kOXXRrTh0Vpn7AcNS00LU74}")
    private String apiKey;

    // Timeout configurado para respuestas largas
    private static final int TIMEOUT_MS = 60000;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    public Mono<GeminiResponse> generateChatResponse(GeminiRequest request) {
        return generateChatResponse(request, 4096); // Default: 4096 tokens
    }

    public Mono<GeminiResponse> generateChatResponse(GeminiRequest request, int maxTokens) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

        Map<String, Object> requestBody = buildRequestBody(request.getMessage(), maxTokens);

        System.out.println("=== INICIO REQUEST GEMINI ===");
        System.out.println("URL: " + url);
        System.out.println("Mensaje: " + request.getMessage());
        System.out.println("Max Tokens: " + maxTokens);
        System.out.println("Request Body: " + requestBody);

        return webClient.post()
                .uri(url)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> {
                    System.out.println("RAW RESPONSE LENGTH: " + response.length());
                    System.out.println("RAW RESPONSE (primeros 500 chars): " +
                            response.substring(0, Math.min(500, response.length())));

                    // Log completo si es respuesta larga
                    if (response.length() > 1000) {
                        System.out.println("RESPUESTA COMPLETA GUARDADA EN LOG LARGO");
                        // Puedes guardar en archivo de log si necesitas
                    }
                    System.out.println("=== FIN REQUEST GEMINI ===");
                })
                .flatMap(response -> processGeminiResponse(response))
                .onErrorResume(error -> {
                    System.out.println("Error llamando a Gemini: " + error.getMessage());
                    error.printStackTrace();
                    return Mono.just(new GeminiResponse(
                            "Error llamando a Gemini: " + error.getMessage(),
                            "gemini-2.5-flash"
                    ));
                });
    }

    private Map<String, Object> buildRequestBody(String message, int maxOutputTokens) {
        Map<String, Object> requestBody = new HashMap<>();

        // Contents array
        List<Map<String, Object>> contentsList = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();

        // Parts array
        List<Map<String, Object>> partsList = new ArrayList<>();
        Map<String, Object> part = new HashMap<>();
        part.put("text", message);
        partsList.add(part);

        content.put("parts", partsList);
        contentsList.add(content);

        requestBody.put("contents", contentsList);

        // Generation config - AUMENTADO SIGNIFICATIVAMENTE
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.7); // Reducido para respuestas más consistentes
        generationConfig.put("maxOutputTokens", maxOutputTokens); // ¡AUMENTADO!
        generationConfig.put("topP", 0.8);
        generationConfig.put("topK", 40);
        requestBody.put("generationConfig", generationConfig);

        // Safety settings (relajados para evitar bloqueos)
        List<Map<String, Object>> safetySettings = new ArrayList<>();
        String[] categories = {"HARM_CATEGORY_HARASSMENT", "HARM_CATEGORY_HATE_SPEECH",
                "HARM_CATEGORY_SEXUALLY_EXPLICIT", "HARM_CATEGORY_DANGEROUS_CONTENT"};

        for (String category : categories) {
            Map<String, Object> setting = new HashMap<>();
            setting.put("category", category);
            setting.put("threshold", "BLOCK_ONLY_HIGH"); // Más permisivo
            safetySettings.add(setting);
        }
        requestBody.put("safetySettings", safetySettings);

        return requestBody;
    }

    private Mono<GeminiResponse> processGeminiResponse(String response) {
        try {
            System.out.println("Procesando respuesta JSON...");
            System.out.println("Longitud de respuesta: " + response.length());

            JsonNode jsonNode = objectMapper.readTree(response);

            // Verificar si hay error primero
            if (jsonNode.has("error")) {
                String errorMsg = jsonNode.get("error").get("message").asText();
                System.out.println("Error de Gemini: " + errorMsg);
                return Mono.just(new GeminiResponse("Error de Gemini: " + errorMsg, "gemini-2.5-flash"));
            }

            // Procesar respuesta exitosa
            if (jsonNode.has("candidates")) {
                JsonNode candidates = jsonNode.get("candidates");
                if (candidates.isArray() && candidates.size() > 0) {
                    JsonNode firstCandidate = candidates.get(0);

                    // Verificar si el candidato fue bloqueado
                    if (firstCandidate.has("finishReason") &&
                            "SAFETY".equals(firstCandidate.get("finishReason").asText())) {
                        return Mono.just(new GeminiResponse(
                                "La respuesta fue bloqueada por configuraciones de seguridad. Intenta con un prompt diferente.",
                                "gemini-2.5-flash"
                        ));
                    }

                    // Procesar contenido
                    if (firstCandidate.has("content")) {
                        JsonNode content = firstCandidate.get("content");
                        if (content.has("parts")) {
                            JsonNode parts = content.get("parts");
                            if (parts.isArray() && parts.size() > 0) {
                                JsonNode firstPart = parts.get(0);
                                if (firstPart.has("text")) {
                                    String text = firstPart.get("text").asText();
                                    System.out.println("Texto extraído - Longitud: " + text.length());
                                    System.out.println("Primeros 200 chars: " +
                                            text.substring(0, Math.min(200, text.length())));

                                    if (text.length() < 50) {
                                        System.out.println("ADVERTENCIA: Respuesta muy corta");
                                    }

                                    return Mono.just(new GeminiResponse(text, "gemini-2.5-flash"));
                                }
                            }
                        }
                    }
                }
            }

            // Si no hay candidatos, verificar promptFeedback
            if (jsonNode.has("promptFeedback")) {
                JsonNode promptFeedback = jsonNode.get("promptFeedback");
                if (promptFeedback.has("blockReason")) {
                    String blockReason = promptFeedback.get("blockReason").asText();
                    System.out.println("Contenido bloqueado: " + blockReason);
                    return Mono.just(new GeminiResponse(
                            "El contenido fue bloqueado por: " + blockReason +
                                    ". Intenta reformular tu pregunta.",
                            "gemini-2.5-flash"
                    ));
                }
            }

            // Log para debug
            System.out.println("Estructura de respuesta no reconocida. Keys: " + jsonNode.fieldNames());
            return Mono.just(new GeminiResponse(
                    "No se pudo procesar la respuesta del modelo. Estructura inesperada.",
                    "gemini-2.5-flash"
            ));

        } catch (Exception e) {
            System.out.println("Error procesando respuesta: " + e.getMessage());
            e.printStackTrace();
            return Mono.just(new GeminiResponse(
                    "Error procesando respuesta: " + e.getMessage(),
                    "gemini-2.5-flash"
            ));
        }
    }

    // Método MEJORADO para rutinas de ejercicio con MÁS TOKENS
    public Mono<GeminiResponse> generateWorkoutRoutine(String goal, String level, String duration) {
        String prompt = String.format("""
            Eres un entrenador personal experto. Genera una rutina de ejercicios COMPLETA para:
            
            OBJETIVO: %s
            NIVEL: %s  
            DURACIÓN: %s
            
            La rutina debe ser PRÁCTICA y EJECUTABLE. Incluye:
            
            • CALENTAMIENTO (5-10 min): ejercicios específicos
            • EJERCICIOS PRINCIPALES: nombre, series, repeticiones, descanso
            • ENFRIAMIENTO: estiramientos
            • RECOMENDACIONES: frecuencia, progresión, precauciones
            
            IMPORTANTE: 
            - Sé CONCISO pero COMPLETO
            - Ejercicios apropiados para nivel %s
            - Duración total: %s
            - Formato CLARO y ORGANIZADO
            - Máximo 8 ejercicios principales
            
            Responde SOLO con la rutina, sin introducciones largas.
            """, goal, level, duration, level, duration);

        System.out.println("=== GENERANDO RUTINA ===");
        System.out.println("Goal: " + goal);
        System.out.println("Level: " + level);
        System.out.println("Duration: " + duration);
        System.out.println("Prompt length: " + prompt.length());

        GeminiRequest request = new GeminiRequest(prompt);

        // ¡AUMENTAMOS LOS TOKENS PARA RUTINAS!
        return generateChatResponse(request, 8192); // 8192 tokens máximo para gemini-2.5-flash
    }

    // Test de conexión
    public Mono<String> testConnection() {
        GeminiRequest request = new GeminiRequest("Responde con 'OK' si funciona.");

        return generateChatResponse(request, 100)
                .map(response -> {
                    if (response.getResponse().contains("Error")) {
                        return "Error: " + response.getResponse();
                    }
                    return "✅ " + response.getResponse();
                })
                .onErrorReturn("Error de conexión con Gemini");
    }
}