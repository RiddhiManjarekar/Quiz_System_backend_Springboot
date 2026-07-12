package com.project.quiz_system.controller;

import com.project.quiz_system.common.ApiResponse;
import com.project.quiz_system.dto.StartQuizResponse;
import com.project.quiz_system.service.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentAttemptController {

    private final QuizAttemptService quizAttemptService;

    @PostMapping("/quizzes/{quizId}/start")
    public ResponseEntity<ApiResponse<StartQuizResponse>> startQuiz(
            @PathVariable Long quizId
    ) {

        StartQuizResponse response =
                quizAttemptService.startQuiz(quizId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<StartQuizResponse>builder()
                                .success(true)
                                .message("Quiz started successfully.")
                                .data(response)
                                .errors(null)
                                .build()
                );

    }

}