package com.project.quiz_system.controller;

import com.project.quiz_system.common.ApiResponse;
import com.project.quiz_system.dto.StartQuizResponse;
import com.project.quiz_system.service.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.project.quiz_system.dto.SubmitQuizRequest;
import com.project.quiz_system.dto.SubmitQuizResponse;
import com.project.quiz_system.dto.ExistingAttemptResponse;
import com.project.quiz_system.dto.ResumeAttemptResponse;
import jakarta.validation.Valid;

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


    @PostMapping("/attempts/{attemptId}/submit")
    public ResponseEntity<ApiResponse<SubmitQuizResponse>> submitQuiz(
            @PathVariable Long attemptId,
            @Valid @RequestBody SubmitQuizRequest request
    ) {

        SubmitQuizResponse response =
                quizAttemptService.submitQuiz(attemptId, request);

        return ResponseEntity.ok(
                ApiResponse.<SubmitQuizResponse>builder()
                        .success(true)
                        .message("Quiz submitted successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    @GetMapping("/quizzes/{quizId}/existing")
    public ResponseEntity<ApiResponse<ExistingAttemptResponse>>
    existingAttempt(
            @PathVariable Long quizId
    ){

        return ResponseEntity.ok(

                ApiResponse.<ExistingAttemptResponse>builder()
                        .success(true)
                        .message("Existing attempt checked.")
                        .data(
                                quizAttemptService
                                        .checkExistingAttempt(
                                                quizId
                                        )
                        )
                        .build()

        );

    }

    @GetMapping("/attempts/{attemptId}")
    public ResponseEntity<ApiResponse<ResumeAttemptResponse>>
    resumeAttempt(
            @PathVariable Long attemptId
    ){

        return ResponseEntity.ok(

                ApiResponse.<ResumeAttemptResponse>builder()
                        .success(true)
                        .message("Attempt restored.")
                        .data(
                                quizAttemptService
                                        .resumeAttempt(
                                                attemptId
                                        )
                        )
                        .build()

        );

    }

}