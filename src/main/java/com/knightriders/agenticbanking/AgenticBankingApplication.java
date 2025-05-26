package com.knightriders.agenticbanking;

import com.knightriders.agenticbanking.tools.BankingTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class AgenticBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgenticBankingApplication.class, args);
    }

}
