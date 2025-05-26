package com.knightriders.agenticbanking.agents;
import dev.langchain4j.service.UserMessage;



public interface AgentWithTools {

    @UserMessage("{{input}}")
    String chat(String input);
}
