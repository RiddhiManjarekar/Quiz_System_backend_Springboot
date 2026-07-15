package com.project.quiz_system.controller;

import com.project.quiz_system.common.ApiResponse;
import com.project.quiz_system.dto.EvaluationRequest;
import com.project.quiz_system.dto.EvaluationResponse;
import com.project.quiz_system.dto.DescriptiveAnswerResponse;
import com.project.quiz_system.dto.PendingEvaluationResponse;
import com.project.quiz_system.dto.EvaluationAttemptResponse;
import com.project.quiz_system.service.TeacherEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.validation.Valid;
@RestController
@RequestMapping(
        "/api/teacher/evaluations"
)
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherEvaluationController {

    private final TeacherEvaluationService
            teacherEvaluationService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<PendingEvaluationResponse>>>
    getPendingAttempts() {

        List<PendingEvaluationResponse> responses =
                teacherEvaluationService.getPendingAttempts();

        return ResponseEntity.ok(
                ApiResponse.<List<PendingEvaluationResponse>>builder()
                        .success(true)
                        .message("Pending evaluations fetched successfully.")
                        .data(responses)
                        .errors(null)
                        .build()
        );
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<ApiResponse<EvaluationAttemptResponse>>
    getAttemptForEvaluation(
            @PathVariable Long attemptId
    ){

        EvaluationAttemptResponse response =
                teacherEvaluationService
                        .getAttemptForEvaluation(attemptId);

        return ResponseEntity.ok(

                ApiResponse.<EvaluationAttemptResponse>builder()
                        .success(true)
                        .message("Evaluation attempt fetched successfully.")
                        .data(response)
                        .errors(null)
                        .build()

        );

    }

    @PatchMapping("/{studentAnswerId}")
    public ResponseEntity<ApiResponse<EvaluationResponse>> evaluate(
            @PathVariable Long studentAnswerId,
            @Valid@RequestBody EvaluationRequest request) {

        EvaluationResponse response =
                teacherEvaluationService.evaluateAnswer(
                        studentAnswerId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.<EvaluationResponse>builder()
                        .success(true)
                        .message("Answer evaluated successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }
}