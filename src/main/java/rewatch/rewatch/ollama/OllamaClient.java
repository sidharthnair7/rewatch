package rewatch.rewatch.ollama;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Talks to Gemma running locally on Ollama. Nothing leaves the user's computer:
 * the only network call is to localhost.
 */
@Component
public class OllamaClient {

    private final RestClient restClient;
    private final String model;

    public OllamaClient(@Value("${rewatch.ollama.base-url}") String baseUrl,
                        @Value("${rewatch.ollama.model}") String model) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.model = model;
    }

    public String model() {
        return model;
    }

    /**
     * Sends one chat turn and returns the model's reply text.
     *
     * @param system the instructions for the model
     * @param user   the user's content (goals and journal)
     * @param schema a JSON schema the reply must follow (Ollama structured outputs)
     */
    @SuppressWarnings("unchecked")
    public String chat(String system, String user, Map<String, Object> schema) {
        Map<String, Object> body = Map.of(
                "model", model,
                "stream", false,
                "format", schema,
                "options", Map.of("temperature", 0.2),
                "messages", List.of(
                        Map.of("role", "system", "content", system),
                        Map.of("role", "user", "content", user)));

        Map<String, Object> response = restClient.post()
                .uri("/api/chat")
                .body(body)
                .retrieve()
                .body(Map.class);

        if (response == null || !(response.get("message") instanceof Map<?, ?> message)) {
            throw new IllegalStateException("Ollama returned no message");
        }
        return String.valueOf(message.get("content"));
    }
}
