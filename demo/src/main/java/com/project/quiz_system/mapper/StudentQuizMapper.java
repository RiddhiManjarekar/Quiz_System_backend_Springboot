package com.project.quiz_system.mapper;

import com.project.quiz_system.dto.StudentOptionResponse;
import com.project.quiz_system.dto.StudentQuestionResponse;
import com.project.quiz_system.dto.StudentQuizResponse;
import com.project.quiz_system.entity.Question;
import com.project.quiz_system.entity.QuestionOption;
import com.project.quiz_system.entity.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class StudentQuizMapper {

    public StudentQuizResponse toResponse(Quiz quiz) {

        return StudentQuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .durationMinutes(quiz.getDurationMinutes())
                .totalMarks(quiz.getTotalMarks())
                .passingMarks(quiz.getPassingMarks())
                .startTime(quiz.getStartTime())
                .endTime(quiz.getEndTime())
                .questions(
                        quiz.getQuestions()
                                .stream()
                                .sorted(
                                        Comparator.comparing(
                                                Question::getDisplayOrder
                                        )
                                )
                                .map(this::toQuestionResponse)
                                .toList()
                )
                .build();
    }

    private StudentQuestionResponse toQuestionResponse(
            Question question
    ) {

        return StudentQuestionResponse.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType())
                .marks(question.getMarks())
                .displayOrder(question.getDisplayOrder())
                .options(
                        question.getOptions()
                                .stream()
                                .map(this::toOptionResponse)
                                .toList()
                )
                .build();
    }

    private StudentOptionResponse toOptionResponse(
            QuestionOption option
    ) {

        return StudentOptionResponse.builder()
                .id(option.getId())
                .optionText(option.getOptionText())
                .build();
    }

}