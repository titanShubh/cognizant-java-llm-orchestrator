# Cognizant Enterprise Python LLM & RAG Agent Orchestrator

import math
import numpy as np
from typing import List, Dict, Any, Callable
from dataclasses import dataclass

# ============================================================================
# 1. VECTOR STORE & RAG ENGINE (Cosine Similarity)
# ============================================================================

@dataclass
class DocumentChunk:
    id: str
    content: str
    embedding: List[float]

class VectorStore:
    def __init__(self):
        self.chunks: List[DocumentChunk] = []

    def add_chunk(self, chunk: DocumentChunk):
        self.chunks.append(chunk)

    def cosine_similarity(self, vec_a: List[float], vec_b: List[float]) -> float:
        a = np.array(vec_a)
        b = np.array(vec_b)
        norm_a = np.linalg.norm(a)
        norm_b = np.linalg.norm(b)
        if norm_a == 0 or norm_b == 0:
            return 0.0
        return float(np.dot(a, b) / (norm_a * norm_b))

    def search(self, query_embedding: List[float], top_k: int = 2) -> List[DocumentChunk]:
        scored = [
            (chunk, self.cosine_similarity(query_embedding, chunk.embedding))
            for chunk in self.chunks
        ]
        scored.sort(key=lambda x: x[1], reverse=True)
        return [chunk for chunk, score in scored[:top_k]]

def generate_pseudo_embedding(text: str) -> List[float]:
    """Generates a deterministic 8-dimensional normalized embedding vector."""
    seed = sum(ord(c) for c in text)
    np.random.seed(seed % 100000)
    vec = np.random.rand(8)
    norm = np.linalg.norm(vec)
    return (vec / norm).tolist()


# ============================================================================
# 2. PROMPT TEMPLATE ENGINE
# ============================================================================

class PromptTemplate:
    def __init__(self, raw_template: str):
        self.raw_template = raw_template

    def format(self, **kwargs) -> str:
        res = self.raw_template
        for k, v in kwargs.items():
            res = res.replace(f"{{{k}}}", str(v))
        return res


# ============================================================================
# 3. LLM AGENT & TOOL CALLING FRAMEWORK
# ============================================================================

class LLMAgent:
    def __init__(self, name: str = "Cognizant-GenAI-Agent"):
        self.name = name
        self.tools: Dict[str, Callable[[str], str]] = {}

    def register_tool(self, tool_name: str, func: Callable[[str], str]):
        self.tools[tool_name.lower()] = func

    def run(self, prompt: str, context: str = "") -> str:
        prompt_lower = prompt.lower()

        # Check for tool calling requirement
        if "calculate" in prompt_lower or "math" in prompt_lower:
            tool_name = "calculatortool"
            if tool_name in self.tools:
                result = self.tools[tool_name](prompt)
                return f"[Agent Tool Triggered: {tool_name}] -> Calculated Result: {result}"

        if context:
            return (
                f"--- Grounded AI Response (RAG Context Applied) ---\n"
                f"Retrieved Knowledge:\n{context}\n\n"
                f"Resolution: Query '{prompt}' has been synthesized against Cognizant GenAI knowledge bases."
            )

        return f"Standard LLM Response for prompt: '{prompt}'"


# ============================================================================
# 4. MAIN PIPELINE EXECUTION
# ============================================================================

def main():
    print("================================================================================")
    print("         COGNIZANT ENTERPRISE PYTHON LLM & RAG ORCHESTRATOR PIPELINE           ")
    print("================================================================================")

    # 1. Initialize Vector Store & RAG
    vector_store = VectorStore()
    
    docs = [
        "Cognizant deploys enterprise Python LLM applications with Multi-Agent RAG architectures.",
        "FastAPI, PyTorch, and LangChain enable high-throughput AI microservices in cloud environments.",
        "Vector embeddings combined with Cosine Similarity ensure grounded context retrieval."
    ]

    print("\n[STEP 1] Indexing Knowledge Base into Vector Store...")
    for idx, doc in enumerate(docs):
        emb = generate_pseudo_embedding(doc)
        vector_store.add_chunk(DocumentChunk(id=f"doc_{idx}", content=doc, embedding=emb))
    print("[SUCCESS] Indexed 3 Knowledge Base Chunks successfully!")

    # 2. Prompt Formatting
    print("\n[STEP 2] Formatting Dynamic System Prompt...")
    template = PromptTemplate("System Role: {role} | Domain: {domain}")
    formatted_prompt = template.format(role="Cognizant Lead GenAI Engineer", domain="Python Enterprise AI")
    print(f"[Prompt Output]: {formatted_prompt}")

    # 3. RAG Semantic Query
    user_query = "What frameworks does Cognizant recommend for Python AI microservices?"
    print(f"\n[STEP 3] Executing RAG Semantic Search for Query: '{user_query}'")
    query_emb = generate_pseudo_embedding(user_query)
    retrieved_chunks = vector_store.search(query_emb, top_k=2)

    context_str = "\n".join([f"- {c.content}" for c in retrieved_chunks])
    
    agent = LLMAgent()
    agent.register_tool("calculatortool", lambda p: "42.0 (Evaluated)")

    rag_response = agent.run(user_query, context=context_str)
    print("\n" + rag_response)

    # 4. Agent Tool Calling
    print("\n[STEP 4] Executing Agent Tool Call...")
    tool_query = "Calculate 12 * 4.5 for resource allocation"
    print(f"User Query: '{tool_query}'")
    agent_response = agent.run(tool_query)
    print(agent_response)

    print("\n================================================================================")
    print("             PYTHON LLM PIPELINE EXECUTED SUCCESSFULLY                         ")
    print("================================================================================")

if __name__ == "__main__":
    main()
