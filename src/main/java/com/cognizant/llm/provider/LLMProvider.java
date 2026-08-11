package com.cognizant.llm.provider;

import com.cognizant.llm.model.ChatMessage;
import java.util.List;

public interface LLMProvider {
    String getProviderName();
    String generateResponse(List<ChatMessage> messages, double temperature);
    double[] generateEmbedding(String text);
}
