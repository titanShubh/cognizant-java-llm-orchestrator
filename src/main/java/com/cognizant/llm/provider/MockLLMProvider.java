package com.cognizant.llm.provider;

import com.cognizant.llm.model.ChatMessage;
import java.util.List;
import java.util.Random;

public class MockLLMProvider implements LLMProvider {

    private final String name;
    private final Random random = new Random(42);

    public MockLLMProvider() {
        this.name = "Mock-GPT4-Orchestrator";
    }

    public MockLLMProvider(String name) {
        this.name = name;
    }

    @Override
    public String getProviderName() {
        return name;
    }

    @Override
    public String generateResponse(List<ChatMessage> messages, double temperature) {
        String lastUserPrompt = "";
        String context = "";

        for (ChatMessage msg : messages) {
            if (msg.getRole() == ChatMessage.Role.USER) {
                lastUserPrompt = msg.getContent();
            } else if (msg.getRole() == ChatMessage.Role.SYSTEM && msg.getContent().contains("Context:")) {
                context = msg.getContent();
            }
        }

        String promptLower = lastUserPrompt.toLowerCase();

        if (promptLower.contains("calculate") || promptLower.contains("multiply") || promptLower.contains("sum")) {
            return "[TOOL_CALL: CalculatorTool(expression=\"" + lastUserPrompt.replaceAll("[^0-9+*-/.]", "") + "\")]";
        }

        if (!context.isEmpty()) {
            return "Based on the retrieved enterprise knowledge base:\n" +
                   "- " + context.replace("Context:\n", "").replace("\n", "\n- ") + "\n\n" +
                   "Conclusion: Synthesizing the above, the query '" + lastUserPrompt + "' is resolved with high confidence.";
        }

        return "Synthesizing AI Response for prompt: '" + lastUserPrompt + "'. The LLM workflow executed cleanly with temperature setting " + temperature + ".";
    }

    @Override
    public double[] generateEmbedding(String text) {
        // Generate deterministic 8-dimensional normalized embedding vector for similarity search
        double[] vector = new double[8];
        int hash = text.hashCode();
        Random r = new Random(hash);
        double sumSquare = 0.0;
        
        for (int i = 0; i < 8; i++) {
            vector[i] = r.nextDouble();
            sumSquare += vector[i] * vector[i];
        }
        
        double norm = Math.sqrt(sumSquare);
        for (int i = 0; i < 8; i++) {
            vector[i] = vector[i] / norm;
        }

        return vector;
    }
}
