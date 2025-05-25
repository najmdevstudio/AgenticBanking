package com.knightriders.agenticbanking.services;

import com.knightriders.agenticbanking.agents.AgentWithTools;
import com.knightriders.agenticbanking.models.Answer;
import com.knightriders.agenticbanking.models.Question;
import com.knightriders.agenticbanking.tools.BankingTools;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class OllamaServiceImpl implements OllamaService {

    private final AgentWithTools agent;

    public OllamaServiceImpl(ChatModel chatModel, BankingTools bankingTools) {
        this.agent = AiServices.builder(AgentWithTools.class)
                .chatModel(chatModel)
                .tools(bankingTools)
                .build();
    }

    @Override
    public String getAnswer(String question) {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("Question cannot be null or empty");
        }
        return agent.chat(question);
    }

    @Override
    public Answer getAnswer(Question question) {
        if (question == null || question.question() == null || question.question().trim().isEmpty()) {
            throw new IllegalArgumentException("Question cannot be null or empty");
        }
        String result = agent.chat(question.question());
        return new Answer(result);
    }
}