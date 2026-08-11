package com.cognizant.llm.vector;

import com.cognizant.llm.model.DocumentChunk;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VectorStore {

    private final List<DocumentChunk> store = new ArrayList<>();

    public void addChunk(DocumentChunk chunk) {
        store.add(chunk);
    }

    public List<DocumentChunk> search(double[] queryEmbedding, int topK) {
        List<ScoredChunk> scoredChunks = new ArrayList<>();

        for (DocumentChunk chunk : store) {
            double score = cosineSimilarity(queryEmbedding, chunk.getEmbedding());
            scoredChunks.add(new ScoredChunk(chunk, score));
        }

        scoredChunks.sort(Comparator.comparingDouble((ScoredChunk sc) -> sc.score).reversed());

        List<DocumentChunk> result = new ArrayList<>();
        for (int i = 0; i < Math.min(topK, scoredChunks.size()); i++) {
            result.add(scoredChunks.get(i).chunk);
        }
        return result;
    }

    private double cosineSimilarity(double[] vectorA, double[] vectorB) {
        if (vectorA.length != vectorB.length) return 0.0;
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += vectorA[i] * vectorA[i];
            normB += vectorB[i] * vectorB[i];
        }

        if (normA == 0 || normB == 0) return 0.0;
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private static class ScoredChunk {
        DocumentChunk chunk;
        double score;

        ScoredChunk(DocumentChunk chunk, double score) {
            this.chunk = chunk;
            this.score = score;
        }
    }
}
