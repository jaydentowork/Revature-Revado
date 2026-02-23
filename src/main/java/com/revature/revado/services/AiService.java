package com.revature.revado.services;

import com.revature.revado.dtos.AiSubtaskReponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class AiService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.api.model}")
    private String model;

    private String baseUrl;

    @Value("classpath:prompts/subtask-generator.txt")
    private Resource SystemPromptResource;

    public AiService(ObjectMapper objectMapper, @Value("${openrouter.api.url}") String baseUrl) {
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.baseUrl = baseUrl;
    }

    public List<String> generateSubtasks(String todoTile, String todoDescription) {
        String systemPrompt;

        // 1. Get the system prompt from our resource file
        try {
            systemPrompt = SystemPromptResource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load AI Prompt");
        }

        // 2. Build the request body using our system prompt + todo title and todo description
        Map<String, Object> requestBody = Map.of("model", model, "messages", List.of(Map.of("role", "system", "content", systemPrompt), Map.of("role", "user", "content", "Task: " + todoTile + todoDescription)), "response_format", Map.of("type", "json_object"));

        try {
            // 3. Send request to open router
            String responseJson = restClient.post().header("Authorization", "Bearer " + apiKey).header("Content-Type", "application/json").body(requestBody).retrieve().body(String.class);

            // 4. Parse json with Jackson library
            JsonNode rootNode = objectMapper.readTree(responseJson);

            // 5. Parse the actual content from the response
            String content = rootNode.path("choices").get(0).path("message").path("content").asText();

            // 6. Map them as AiSubtask Class and return
            AiSubtaskReponse aiResponse = objectMapper.readValue(content, AiSubtaskReponse.class);
            return aiResponse.getSubtasks();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate subtasks from AI: " + e.getMessage());
        }
    }
}
