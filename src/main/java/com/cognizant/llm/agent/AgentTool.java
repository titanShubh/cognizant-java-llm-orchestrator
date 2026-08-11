package com.cognizant.llm.agent;

public interface AgentTool {
    String getToolName();
    String getToolDescription();
    String execute(String input);
}
