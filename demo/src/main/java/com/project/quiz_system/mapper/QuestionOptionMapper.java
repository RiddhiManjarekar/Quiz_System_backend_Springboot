package com.project.quiz_system.mapper;

import com.project.quiz_system.dto.QuestionOptionRequest;
import com.project.quiz_system.dto.QuestionOptionResponse;
import com.project.quiz_system.entity.Question;
import com.project.quiz_system.entity.QuestionOption;
import org.springframework.stereotype.Component;

@Component
public class QuestionOptionMapper {

    public QuestionOption toEntity(
            QuestionOptionRequest request,
            Question question
    ) {

        return QuestionOption.builder()
                .question(question)
                .optionText(request.getOptionText())
                .correctAnswer(request.getCorrectAnswer())
                .build();
    }

    public QuestionOptionResponse toResponse(
            QuestionOption option
    ) {

        return QuestionOptionResponse.builder()
                .id(option.getId())
                .questionId(option.getQuestion().getId())
                .optionText(option.getOptionText())
                .correctAnswer(option.getCorrectAnswer())
                .build();
    }

    public void updateEntity(
            QuestionOption option,
            QuestionOptionRequest request
    ) {

        option.setOptionText(
                request.getOptionText()
        );

        option.setCorrectAnswer(
                request.getCorrectAnswer()
        );

    }

}