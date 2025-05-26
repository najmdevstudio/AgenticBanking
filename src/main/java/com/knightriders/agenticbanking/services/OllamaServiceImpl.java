package com.knightriders.agenticbanking.services;


import com.knightriders.agenticbanking.agents.AgentWithTools;
import com.knightriders.agenticbanking.models.Answer;
import com.knightriders.agenticbanking.models.Question;
import com.knightriders.agenticbanking.tools.BankingTools;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Service;

@Service
public class OllamaServiceImpl implements OllamaService {

    private final AgentWithTools agent;





    public OllamaServiceImpl(OpenAiChatModel chatModel, BankingTools bankingTools) {
        this.agent = AiServices.builder(AgentWithTools.class)
                .chatModel(chatModel)
                .tools(bankingTools)
                .build();


    }

    @Override
    public String getAnswer(String question) {
        return agent.chat(question);
    }

    @Override
    public Answer getAnswer(Question question) {
        String result = agent.chat(question.question());
        return new Answer(result);
    }



}
