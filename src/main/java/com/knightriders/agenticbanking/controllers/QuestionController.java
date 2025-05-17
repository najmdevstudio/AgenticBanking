package com.knightriders.agenticbanking.controllers;

import com.knightriders.agenticbanking.models.Answer;
import com.knightriders.agenticbanking.models.Question;
import com.knightriders.agenticbanking.services.OllamaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

    private final OllamaService ollamaService;

    public QuestionController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question){
        return ollamaService.getAnswer(question);
    }
}
