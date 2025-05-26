package com.knightriders.agenticbanking.agents;
import dev.langchain4j.service.UserMessage;

public interface AgentWithTools {

    @UserMessage("""
            You are a helpful banking assistant. 
            You have access to an MCP Server with Several Banking Tools
            Answer the following question: {{question}}
            """
    )
    String chat(String question);
}
