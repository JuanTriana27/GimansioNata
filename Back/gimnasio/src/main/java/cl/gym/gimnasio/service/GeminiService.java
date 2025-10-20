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

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    public Mono<GeminiResponse> generateChatResponse(GeminiRequest request) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

        Map<String, Object> requestBody = buildRequestBody(request.getMessage());

        System.out.println("=== INICIO REQUEST GEMINI ===");
        System.out.println("URL: " + url);
        System.out.println("Mensaje: " + request.getMessage());
        System.out.println("Request Body: " + requestBody);

        return webClient.post()
                .uri(url)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> {
                    System.out.println("RAW RESPONSE: " + response);
                    System.out.println("=== FIN REQUEST GEMINI ===");
                })
                .flatMap(this::processGeminiResponse)
                .onErrorResume(error -> {
                    System.out.println("Error: " + error.getMessage());
                    return Mono.just(new GeminiResponse(
                            "Error llamando a Gemini: " + error.getMessage(),
                            "gemini-2.5-flash"
                    ));
                });
    }

    private Map<String, Object> buildRequestBody(String message) {
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

        // Generation config
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.9); // Más creativo
        generationConfig.put("maxOutputTokens", 2048); // Más largo
        generationConfig.put("topP", 0.8);
        generationConfig.put("topK", 40);
        requestBody.put("generationConfig", generationConfig);

        // Safety settings (para evitar bloqueos)
        List<Map<String, Object>> safetySettings = new ArrayList<>();
        String[] categories = {"HARM_CATEGORY_HARASSMENT", "HARM_CATEGORY_HATE_SPEECH",
                "HARM_CATEGORY_SEXUALLY_EXPLICIT", "HARM_CATEGORY_DANGEROUS_CONTENT"};

        for (String category : categories) {
            Map<String, Object> setting = new HashMap<>();
            setting.put("category", category);
            setting.put("threshold", "BLOCK_MEDIUM_AND_ABOVE");
            safetySettings.add(setting);
        }
        requestBody.put("safetySettings", safetySettings);

        return requestBody;
    }

    private Mono<GeminiResponse> processGeminiResponse(String response) {
        try {
            System.out.println("Procesando respuesta JSON...");

            JsonNode jsonNode = objectMapper.readTree(response);

            // Verificar si hay error primero
            if (jsonNode.has("error")) {
                String errorMsg = jsonNode.get("error").get("message").asText();
                System.out.println("Error de Gemini: " + errorMsg);
                return Mono.just(new GeminiResponse("Error de Gemini: " + errorMsg, "gemini-2.5-flash"));
            }

            // Procesar respuesta exitosa - manera más robusta
            if (jsonNode.has("candidates")) {
                JsonNode candidates = jsonNode.get("candidates");
                if (candidates.isArray() && candidates.size() > 0) {
                    JsonNode firstCandidate = candidates.get(0);

                    // Verificar si el candidato tiene content
                    if (firstCandidate.has("content")) {
                        JsonNode content = firstCandidate.get("content");
                        if (content.has("parts")) {
                            JsonNode parts = content.get("parts");
                            if (parts.isArray() && parts.size() > 0) {
                                JsonNode firstPart = parts.get(0);
                                if (firstPart.has("text")) {
                                    String text = firstPart.get("text").asText();
                                    System.out.println("Texto extraído: " + text.substring(0, Math.min(100, text.length())) + "...");
                                    return Mono.just(new GeminiResponse(text, "gemini-2.5-flash"));
                                }
                            }
                        }
                    }

                    // Si hay candidato pero no tiene la estructura esperada
                    System.out.println("Candidato encontrado pero estructura inesperada: " + firstCandidate);
                }
            }

            // Si no hay candidatos, verificar si hay promptFeedback (bloqueo)
            if (jsonNode.has("promptFeedback")) {
                JsonNode promptFeedback = jsonNode.get("promptFeedback");
                if (promptFeedback.has("blockReason")) {
                    String blockReason = promptFeedback.get("blockReason").asText();
                    System.out.println("Contenido bloqueado: " + blockReason);
                    return Mono.just(new GeminiResponse(
                            "El contenido fue bloqueado por: " + blockReason +
                                    ". Intenta con un prompt diferente.",
                            "gemini-2.5-flash"
                    ));
                }
            }

            // Si llegamos aquí, mostrar la respuesta completa para debug
            System.out.println("Estructura de respuesta no reconocida. Respuesta completa: " + response);
            return Mono.just(new GeminiResponse(
                    "No se pudo obtener respuesta del modelo. Estructura inesperada.",
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

    // Método MEJORADO para rutinas de ejercicio
    public Mono<GeminiResponse> generateWorkoutRoutine(String goal, String level, String duration) {
        String prompt = String.format("""
            Eres un entrenador personal experto en fitness. Crea una rutina de ejercicios COMPLETA y DETALLADA para:
            
            OBJETIVO: %s
            NIVEL: %s
            DURACIÓN: %s
            
            La rutina debe incluir:
            
            1. CALENTAMIENTO (5-10 minutos):
               - Ejercicios específicos de calentamiento
               - Duración de cada ejercicio
            
            2. EJERCICIOS PRINCIPALES:
               - Nombre del ejercicio
               - Series y repeticiones
               - Descanso entre series
               - Técnica básica
            
            3. ENFRIAMIENTO (5 minutos):
               - Estiramientos específicos
               - Tiempo de cada estiramiento
            
            4. RECOMENDACIONES:
               - Frecuencia semanal
               - Progresión
               - Precauciones
               - Consejos de alimentación e hidratación
            
            IMPORTANTE: 
            - Sé MUY específico y detallado
            - Usa ejercicios apropiados para el nivel %s
            - Asegúrate de que la rutina ocupe exactamente %s
            - Incluye al menos 5-8 ejercicios principales
            - Formatea la respuesta de manera clara y organizada
            
            Responde SOLO con la rutina de ejercicios, sin introducciones largas.
            """, goal, level, duration, level, duration);

        System.out.println("Generando rutina para: " + goal + " | " + level + " | " + duration);

        GeminiRequest request = new GeminiRequest(prompt);
        return generateChatResponse(request);
    }

    // Test de conexión mejorado
    public Mono<String> testConnection() {
        GeminiRequest request = new GeminiRequest("Responde brevemente con 'OK' para confirmar que el servicio funciona correctamente.");

        return generateChatResponse(request)
                .map(response -> {
                    if (response.getResponse().contains("Error") || response.getResponse().contains("bloqueado")) {
                        return response.getResponse();
                    }
                    return "✅ " + response.getResponse();
                })
                .onErrorReturn("Error de conexión con Gemini");
    }
}