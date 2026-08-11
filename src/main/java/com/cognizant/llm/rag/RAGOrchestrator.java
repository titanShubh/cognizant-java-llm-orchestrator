package com.cognizant.llm.rag;

import com.cognizant.llm.model.ChatMessage;
import com.cognizant.llm.model.DocumentChunk;
import com.cognizant.llm.provider.LLMProvider;
import com.cognizant.llm.vector.VectorStore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RAGOrchestrator {

    private final VectorStore vectorStore;
    private final LLMProvider llmProvider;

    public RAGOrchestrator(VectorStore vectorStore, LLMProvider llmProvider) {
        this.vectorStore = vectorStore;
        this.llmProvider = llmProvider;
    }

    public void indexDocument(String text) {
        double[] embedding = llmProvider.generateEmbedding(text);
        DocumentChunk chunk = new DocumentChunk(UUID.randomUUID().toString(), text, embedding);
        vectorStore.addChunk(chunk);
    }

    public String query(String userPrompt) {
        // 1. Generate query embedding
        double[] queryVector = llmProvider.generateEmbedding(userPrompt);

        // 2. Retrieve top-K relevant contexts from Vector Store
        List<DocumentChunk> retrievedChunks = vectorStore.search(queryVector, 2);

        // 3. Construct Context payload
        StringBuilder contextBuilder = new StringBuilder("Context:\n");
        for (DocumentChunk chunk : retrievedChunks) {
            contextBuilder.append(chunk.getContent()).append("\n");
        }

        // 4. Build Chat Messages with System Prompt + Context + User Prompt
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessage.Role.SYSTEM, "You are a Cognizant Enterprise AI Assistant. Use the provided context to answer accurately."));
        messages.add(new ChatMessage(ChatMessage.Role.SYSTEM, contextBuilder.toString()));
        messages.add(new ChatMessage(ChatMessage.Role.USER, userPrompt));

        // 5. Generate LLM Answer
        return llmProvider.generateResponse(messages, 0.2);
    }
}
