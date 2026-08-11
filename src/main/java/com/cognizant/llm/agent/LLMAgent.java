package com.cognizant.llm.agent;

import com.cognizant.llm.model.ChatMessage;
import com.cognizant.llm.provider.LLMProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LLMAgent {

    private final LLMProvider llmProvider;
    private final Map<String, AgentTool> tools = new HashMap<>();

    public LLMAgent(LLMProvider llmProvider) {
        this.llmProvider = llmProvider;
    }

    public void registerTool(AgentTool tool) {
        tools.put(tool.getToolName().toLowerCase(), tool);
    }

    public String run(String prompt) {
        List<ChatMessage> history = new ArrayList<>();
        history.add(new ChatMessage(ChatMessage.Role.SYSTEM, "You are an autonomous LLM Agent equipped with function calling capabilities."));
        history.add(new ChatMessage(ChatMessage.Role.USER, prompt));

        String rawResponse = llmProvider.generateResponse(history, 0.0);

        // Check if LLM requested a tool call
        if (rawResponse.startsWith("[TOOL_CALL:")) {
            System.out.println("[Agent Logic] Detected Tool Execution Request from LLM...");
            
            // Execute default registered tool logic (e.g. Calculator)
            for (AgentTool tool : tools.values()) {
                String toolResult = tool.execute(prompt);
                System.out.println("[Agent Logic] Executed Tool '" + tool.getToolName() + "' -> Result: " + toolResult);
                return "Agent Response: Successfully executed " + tool.getToolName() + ". Calculated Value = " + toolResult;
            }
        }

        return rawResponse;
    }
}
