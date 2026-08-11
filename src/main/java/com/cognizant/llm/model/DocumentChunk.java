package com.cognizant.llm.model;

import java.util.Arrays;

public class DocumentChunk {
    private final String id;
    private final String content;
    private final double[] embedding;

    public DocumentChunk(String id, String content, double[] embedding) {
        this.id = id;
        this.content = content;
        this.embedding = embedding;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public double[] getEmbedding() {
        return embedding;
    }

    @Override
    public String toString() {
        return "DocumentChunk{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
