package com.project.quiz_system.controller;

import com.project.quiz_system.dto.QuizRequest;
import com.project.quiz_system.dto.QuizResponse;
import com.project.quiz_system.dto.QuizStatusRequest;
import com.project.quiz_system.service.QuizService;
import com.project.quiz_system.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/teacher/quizzes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuizResponse>> createQuiz(
            @Valid @RequestBody QuizRequest request) {

        QuizResponse response = quizService.createQuiz(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<QuizResponse>builder()
                                .success(true)
                                .message("Quiz created successfully.")
                                .data(response)
                                .errors(null)
                                .build()
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<QuizResponse>>> getMyQuizzes() {

        List<QuizResponse> quizzes = quizService.getMyQuizzes();

        return ResponseEntity.ok(
                ApiResponse.<List<QuizResponse>>builder()
                        .success(true)
                        .message("Quizzes fetched successfully.")
                        .data(quizzes)
                        .errors(null)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponse>> getQuizById(
            @PathVariable Long id) {

        QuizResponse quiz = quizService.getQuizById(id);

        return ResponseEntity.ok(
                ApiResponse.<QuizResponse>builder()
                        .success(true)
                        .message("Quiz fetched successfully.")
                        .data(quiz)
                        .errors(null)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponse>> updateQuiz(
            @PathVariable Long id,
            @Valid @RequestBody QuizRequest request) {

        QuizResponse response = quizService.updateQuiz(id, request);

        return ResponseEntity.ok(
                        ApiResponse.<QuizResponse>builder()
                                .success(true)
                                .message("Quiz updated successfully.")
                                .data(response)
                                .errors(null)
                                .build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(
            @PathVariable Long id) {

        quizService.deleteQuiz(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Quiz deleted successfully.")
                        .data(null)
                        .errors(null)
                        .build()
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<QuizResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody QuizStatusRequest request) {

        QuizResponse response = quizService.updateQuizStatus(id, request);
        return ResponseEntity.ok(
                        ApiResponse.<QuizResponse>builder()
                                .success(true)
                                .message("Quiz status updated successfully.")
                                .data(response)
                                .errors(null)
                                .build()
                );
    }
}