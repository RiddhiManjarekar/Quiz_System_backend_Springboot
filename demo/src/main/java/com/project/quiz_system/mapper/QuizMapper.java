package com.project.quiz_system.mapper;

import com.project.quiz_system.dto.QuizRequest;
import com.project.quiz_system.dto.QuizResponse;
import com.project.quiz_system.entity.Quiz;
import com.project.quiz_system.entity.User;
import org.springframework.stereotype.Component;

@Component
public class QuizMapper {

    public Quiz toEntity(QuizRequest request, User teacher) {

        return Quiz.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .durationMinutes(request.getDurationMinutes())
                .passingMarks(request.getPassingMarks())
                .teacher(teacher)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
    }

    public QuizResponse toResponse(Quiz quiz) {

        return QuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .durationMinutes(quiz.getDurationMinutes())
                .totalMarks(quiz.getTotalMarks())
                .passingMarks(quiz.getPassingMarks())
                .status(quiz.getStatus())
                .startTime(quiz.getStartTime())
                .endTime(quiz.getEndTime())
                .teacherId(quiz.getTeacher().getId())
                .teacherName(quiz.getTeacher().getName())
                .build();
    }

    public void updateEntity(
            Quiz quiz,
            QuizRequest request){

        quiz.setTitle(request.getTitle());

        quiz.setDescription(request.getDescription());

        quiz.setDurationMinutes(
                request.getDurationMinutes());

        quiz.setPassingMarks(
                request.getPassingMarks());

        quiz.setStartTime(
                request.getStartTime());

        quiz.setEndTime(
                request.getEndTime());

    }
}