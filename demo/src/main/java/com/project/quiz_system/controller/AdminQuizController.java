package com.project.quiz_system.controller;

import com.project.quiz_system.common.ApiResponse;
import com.project.quiz_system.dto.QuizResponse;
import com.project.quiz_system.dto.QuizStatusRequest;
import com.project.quiz_system.enums.QuizStatus;
import com.project.quiz_system.service.AdminQuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/quizzes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminQuizController {

    private final AdminQuizService adminQuizService;

    /*
     * GET ALL QUIZZES
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<QuizResponse>>> getAllQuizzes() {

        List<QuizResponse> quizzes =
                adminQuizService.getAllQuizzes();

        return ResponseEntity.ok(
                ApiResponse.<List<QuizResponse>>builder()
                        .success(true)
                        .message("Quizzes fetched successfully.")
                        .data(quizzes)
                        .errors(null)
                        .build()
        );
    }

    /*
     * GET QUIZ BY ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponse>> getQuizById(
            @PathVariable Long id
    ) {

        QuizResponse response =
                adminQuizService.getQuizById(id);

        return ResponseEntity.ok(
                ApiResponse.<QuizResponse>builder()
                        .success(true)
                        .message("Quiz fetched successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    /*
     * GET QUIZZES BY STATUS
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<QuizResponse>>> getByStatus(
            @PathVariable QuizStatus status
    ) {

        List<QuizResponse> quizzes =
                adminQuizService.getQuizzesByStatus(status);

        return ResponseEntity.ok(
                ApiResponse.<List<QuizResponse>>builder()
                        .success(true)
                        .message("Quizzes fetched successfully.")
                        .data(quizzes)
                        .errors(null)
                        .build()
        );
    }

    /*
     * UPDATE QUIZ STATUS
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<QuizResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody QuizStatusRequest request
    ) {

        QuizResponse response =
                adminQuizService.updateQuizStatus(
                        id,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.<QuizResponse>builder()
                        .success(true)
                        .message("Quiz status updated successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    /*
     * DELETE QUIZ
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(
            @PathVariable Long id
    ) {

        adminQuizService.deleteQuiz(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Quiz deleted successfully.")
                        .data(null)
                        .errors(null)
                        .build()
        );
    }

}