package cl.gym.gimnasio.controller;

import cl.gym.gimnasio.dto.request.GeminiRequest;
import cl.gym.gimnasio.dto.response.GeminiResponse;
import cl.gym.gimnasio.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // Endpoint básico de chat con más tokens
    @PostMapping("/chat")
    public Mono<ResponseEntity<GeminiResponse>> chat(@RequestBody GeminiRequest request) {
        return geminiService.generateChatResponse(request, 4096) // Aumentado a 4096
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build())
                .timeout(Duration.ofSeconds(45)); // Timeout de 45 segundos
    }

    // Endpoint para chat con contexto de gimnasio
    @PostMapping("/chat/fitness")
    public Mono<ResponseEntity<GeminiResponse>> fitnessChat(@RequestBody GeminiRequest request) {
        String fitnessContext = "Eres un asistente especializado en gimnasio, fitness y salud. " +
                "Proporciona respuestas COMPLETAS, prácticas y profesionales sobre: " +
                "ejercicios, rutinas de entrenamiento, nutrición deportiva, técnicas adecuadas, " +
                "prevención de lesiones y suplementación. Responde en español de manera clara y organizada.";

        String promptWithContext = fitnessContext + "\n\nPregunta: " + request.getMessage();
        GeminiRequest fitnessRequest = new GeminiRequest(promptWithContext);

        return geminiService.generateChatResponse(fitnessRequest, 4096) // 4096 tokens
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build())
                .timeout(Duration.ofSeconds(45));
    }

    // Endpoint específico para generar rutinas
    @PostMapping("/workout-routine")
    public Mono<ResponseEntity<GeminiResponse>> generateWorkoutRoutine(
            @RequestBody Map<String, String> request) {

        String goal = request.get("goal");
        String level = request.get("level");
        String duration = request.get("duration");

        if (goal == null || level == null || duration == null) {
            return Mono.just(ResponseEntity.badRequest().body(
                    new GeminiResponse("Faltan parámetros: goal, level, duration", "error")
            ));
        }

        return geminiService.generateWorkoutRoutine(goal, level, duration)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build())
                .timeout(Duration.ofSeconds(60)); // 60 segundos para rutinas
    }

    // Endpoint GET simple
    @GetMapping("/chat")
    public Mono<ResponseEntity<GeminiResponse>> chatGet(@RequestParam String message) {
        GeminiRequest request = new GeminiRequest(message);
        return geminiService.generateChatResponse(request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    // Endpoint de diagnóstico
    @GetMapping("/test")
    public Mono<ResponseEntity<Map<String, String>>> testConnection() {
        return geminiService.testConnection()
                .map(result -> ResponseEntity.ok(Map.of(
                        "status", result,
                        "timestamp", java.time.LocalDateTime.now().toString()
                )))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    // Endpoint de salud
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "service", "Google Gemini AI",
                "status", "active",
                "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}