package com.cognizant.llm.model;

import java.util.Map;

public class PromptTemplate {
    private final String rawTemplate;

    public PromptTemplate(String rawTemplate) {
        this.rawTemplate = rawTemplate;
    }

    public String format(Map<String, String> variables) {
        String result = rawTemplate;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    public String getRawTemplate() {
        return rawTemplate;
    }
}
