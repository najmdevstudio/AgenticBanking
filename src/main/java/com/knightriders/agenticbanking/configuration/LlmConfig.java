package com.knightriders.agenticbanking.configuration;

import dev.langchain4j.http.client.spring.restclient.SpringRestClientBuilder;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.langchain4j.model.ollama.OllamaChatModel;

@Configuration
public class LlmConfig {

    @Bean
    public OllamaChatModel ollamaChatModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("deepseek-r1:8b")
                .httpClientBuilder(new SpringRestClientBuilder())
                .build();
    }

    @Bean
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.builder()
                .apiKey("")  // Or use environment variable
                .modelName("gpt-4-1106-preview")
                .httpClientBuilder(new SpringRestClientBuilder())// supports tool calling
                .temperature(0.7)
                .build();
    }

}
