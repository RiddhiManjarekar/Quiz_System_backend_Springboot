package com.project.quiz_system.controller;

import com.project.quiz_system.common.ApiResponse;
import com.project.quiz_system.dto.StudentQuizResponse;
import com.project.quiz_system.service.StudentQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/quizzes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentQuizController {

    private final StudentQuizService studentQuizService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentQuizResponse>>> getAvailableQuizzes() {

        List<StudentQuizResponse> quizzes =
                studentQuizService.getAvailableQuizzes();

        return ResponseEntity.ok(
                ApiResponse.<List<StudentQuizResponse>>builder()
                        .success(true)
                        .message("Available quizzes fetched successfully.")
                        .data(quizzes)
                        .errors(null)
                        .build()
        );
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<ApiResponse<StudentQuizResponse>> getQuiz(
            @PathVariable Long quizId
    ) {

        StudentQuizResponse response =
                studentQuizService.getQuiz(quizId);

        return ResponseEntity.ok(
                ApiResponse.<StudentQuizResponse>builder()
                        .success(true)
                        .message("Quiz fetched successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

}