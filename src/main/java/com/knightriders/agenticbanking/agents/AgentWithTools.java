package com.knightriders.agenticbanking.agents;
import dev.langchain4j.service.UserMessage;

public interface AgentWithTools {

    @UserMessage("{{message}}")
    String chat(String message);
}
