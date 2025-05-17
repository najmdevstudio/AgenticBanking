package com.knightriders.agenticbanking.services;

import com.knightriders.agenticbanking.models.Answer;
import com.knightriders.agenticbanking.models.Question;

public interface OllamaService {
    String getAnswer(String question);
    Answer getAnswer(Question question);
}
