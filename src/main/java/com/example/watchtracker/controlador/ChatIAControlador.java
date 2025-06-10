package com.example.watchtracker.controlador;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/chat")
public class ChatIAControlador {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${ia.demo.activa:true}") // Puedes cambiar esto desde application.properties
    private boolean iaActiva;

    private final RestTemplate restTemplate = new RestTemplate();

    // ⚠️ Mapa temporal en memoria (idealmente usar base de datos)
    private final Map<String, Map<LocalDate, Integer>> contadorMensajesPorUsuario = new HashMap<>();
    private static final int LIMITE_DIARIO = 5;

    @PostMapping
    public ResponseEntity<Map<String, String>> procesarMensaje(@RequestBody Map<String, String> request,
                                                               @RequestHeader("username") String username) {
        Map<String, String> respuesta = new HashMap<>();

        if (!iaActiva) {
            respuesta.put("respuesta", "🔒 La IA está desactivada por el administrador.");
            return ResponseEntity.ok(respuesta);
        }

        LocalDate hoy = LocalDate.now();
        contadorMensajesPorUsuario.putIfAbsent(username, new HashMap<>());
        Map<LocalDate, Integer> historial = contadorMensajesPorUsuario.get(username);
        historial.putIfAbsent(hoy, 0);

        if (historial.get(hoy) >= LIMITE_DIARIO) {
            respuesta.put("respuesta", "⏳ Has alcanzado el límite de 5 mensajes diarios con la IA.");
            return ResponseEntity.ok(respuesta);
        }

        // Procesar mensaje y aumentar contador
        String mensajeUsuario = request.get("mensaje");
        String respuestaIA = llamarAPIChatGPT(mensajeUsuario);
        historial.put(hoy, historial.get(hoy) + 1);

        respuesta.put("respuesta", respuestaIA);
        return ResponseEntity.ok(respuesta);
    }

    private String llamarAPIChatGPT(String mensaje) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");

        List<Map<String, String>> mensajes = new ArrayList<>();
        mensajes.add(Map.of("role", "system", "content", "Eres un experto en relojes que responde de forma concisa."));
        mensajes.add(Map.of("role", "user", "content", mensaje));
        body.put("messages", mensajes);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                List choices = (List) response.getBody().get("choices");
                Map choice = (Map) choices.get(0);
                Map message = (Map) choice.get("message");
                return message.get("content").toString().trim();
            }
        } catch (Exception e) {
            return "⚠️ Error al contactar con la IA.";
        }

        return "⚠️ No se pudo obtener respuesta.";
    }
}
