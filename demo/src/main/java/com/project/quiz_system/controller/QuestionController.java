package com.project.quiz_system.controller;

import com.project.quiz_system.common.ApiResponse;
import com.project.quiz_system.dto.QuestionRequest;
import com.project.quiz_system.dto.QuestionResponse;
import com.project.quiz_system.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<ApiResponse<QuestionResponse>> createQuestion(
            @PathVariable Long quizId,
            @Valid @RequestBody QuestionRequest request
    ) {

        QuestionResponse response =
                questionService.createQuestion(
                        quizId,
                        request
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<QuestionResponse>builder()
                                .success(true)
                                .message("Question created successfully.")
                                .data(response)
                                .errors(null)
                                .build()
                );
    }

    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestionsByQuiz(
            @PathVariable Long quizId
    ) {

        List<QuestionResponse> response =
                questionService.getQuestionsByQuiz(
                        quizId
                );

        return ResponseEntity.ok(
                ApiResponse.<List<QuestionResponse>>builder()
                        .success(true)
                        .message("Questions fetched successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<ApiResponse<QuestionResponse>> getQuestionById(
            @PathVariable Long questionId
    ) {

        QuestionResponse response =
                questionService.getQuestionById(
                        questionId
                );

        return ResponseEntity.ok(
                ApiResponse.<QuestionResponse>builder()
                        .success(true)
                        .message("Question fetched successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    @PutMapping("/questions/{questionId}")
    public ResponseEntity<ApiResponse<QuestionResponse>> updateQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody QuestionRequest request
    ) {

        QuestionResponse response =
                questionService.updateQuestion(
                        questionId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.<QuestionResponse>builder()
                        .success(true)
                        .message("Question updated successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(
            @PathVariable Long questionId
    ) {

        questionService.deleteQuestion(questionId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Question deleted successfully.")
                        .data(null)
                        .errors(null)
                        .build()
        );
    }
}