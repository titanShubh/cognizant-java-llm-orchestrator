package com.cognizant.llm;

import com.cognizant.llm.agent.AgentTool;
import com.cognizant.llm.agent.LLMAgent;
import com.cognizant.llm.model.PromptTemplate;
import com.cognizant.llm.provider.MockLLMProvider;
import com.cognizant.llm.rag.RAGOrchestrator;
import com.cognizant.llm.vector.VectorStore;

import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("            COGNIZANT ENTERPRISE JAVA LLM ORCHESTRATION PIPELINE                ");
        System.out.println("================================================================================");

        // 1. Initialize LLM Provider & Vector Store
        MockLLMProvider llmProvider = new MockLLMProvider("Cognizant-Enterprise-LLM");
        VectorStore vectorStore = new VectorStore();
        RAGOrchestrator ragPipeline = new RAGOrchestrator(vectorStore, llmProvider);

        System.out.println("\n[STEP 1] Indexing Enterprise Knowledge Base Into Vector Store...");
        ragPipeline.indexDocument("Cognizant provides enterprise GenAI solutions including RAG pipelines and custom LLM microservices.");
        ragPipeline.indexDocument("Java 17 and Spring AI enable production-grade asynchronous LLM workflow orchestration.");
        ragPipeline.indexDocument("LLM Agents combine system prompt engineering with vector embeddings and function calling tools.");
        System.out.println("[SUCCESS] Vector Indexing Complete!");

        // 2. Prompt Template Formatting Demonstration
        System.out.println("\n[STEP 2] Formatting Dynamic System Prompt Template...");
        PromptTemplate template = new PromptTemplate(
            "System Prompt: You are {role} specializing in {domain}. Context domain: {context}."
        );
        Map<String, String> variables = new HashMap<>();
        variables.put("role", "Cognizant AI Architect");
        variables.put("domain", "Enterprise Java LLM Integration");
        variables.put("context", "Production Cloud Microservices");
        
        String formattedPrompt = template.format(variables);
        System.out.println("[Prompt Template Output]: " + formattedPrompt);

        // 3. RAG Query Execution
        System.out.println("\n[STEP 3] Executing RAG Pipeline Semantic Query...");
        String query = "What GenAI solutions and Java frameworks does Cognizant recommend?";
        System.out.println("User Query: " + query);
        String ragResponse = ragPipeline.query(query);
        System.out.println("\n--- RAG Response Output ---");
        System.out.println(ragResponse);

        // 4. LLM Agent Tool Calling Demonstration
        System.out.println("\n[STEP 4] Executing LLM Agent Tool Call Workflow...");
        LLMAgent agent = new LLMAgent(llmProvider);
        agent.registerTool(new AgentTool() {
            @Override
            public String getToolName() {
                return "CalculatorTool";
            }

            @Override
            public String getToolDescription() {
                return "Performs arithmetic evaluation for financial calculations.";
            }

            @Override
            public String execute(String input) {
                return "42.0 (Calculated successfully)";
            }
        });

        String agentPrompt = "Calculate 15 * 2.8 for enterprise resource estimation";
        System.out.println("User Agent Prompt: " + agentPrompt);
        String agentResponse = agent.run(agentPrompt);
        System.out.println("\n--- Agent Execution Output ---");
        System.out.println(agentResponse);

        System.out.println("\n================================================================================");
        System.out.println("         COGNIZANT JAVA LLM ORCHESTRATOR PIPELINE EXECUTED CLEANLY             ");
        System.out.println("================================================================================");
    }
}
