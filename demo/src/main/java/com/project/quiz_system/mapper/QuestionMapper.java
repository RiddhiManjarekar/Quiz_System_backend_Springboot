package com.project.quiz_system.mapper;

import com.project.quiz_system.dto.QuestionRequest;
import com.project.quiz_system.dto.QuestionResponse;
import com.project.quiz_system.entity.Question;
import com.project.quiz_system.entity.Quiz;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public Question toEntity(
            QuestionRequest request,
            Quiz quiz
    ) {

        return Question.builder()
                .quiz(quiz)
                .questionText(request.getQuestionText())
                .questionType(request.getQuestionType())
                .marks(request.getMarks())
                .negativeMarks(
                        request.getNegativeMarks() == null
                                ? 0.0
                                : request.getNegativeMarks()
                )
                .displayOrder(request.getDisplayOrder())
                .build();
    }

    public QuestionResponse toResponse(
            Question question
    ) {

        return QuestionResponse.builder()
                .id(question.getId())
                .quizId(question.getQuiz().getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType())
                .marks(question.getMarks())
                .negativeMarks(question.getNegativeMarks())
                .displayOrder(question.getDisplayOrder())
                .build();
    }

    public void updateEntity(
            Question question,
            QuestionRequest request
    ) {

        question.setQuestionText(request.getQuestionText());

        question.setQuestionType(request.getQuestionType());

        question.setMarks(request.getMarks());

        question.setNegativeMarks(
                request.getNegativeMarks() == null
                        ? 0.0
                        : request.getNegativeMarks()
        );

        question.setDisplayOrder(request.getDisplayOrder());
    }
}