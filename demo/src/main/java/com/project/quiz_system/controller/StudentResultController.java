package com.project.quiz_system.controller;

import com.project.quiz_system.common.ApiResponse;
import com.project.quiz_system.dto.ResultDetailResponse;
import com.project.quiz_system.dto.ResultSummaryResponse;
import com.project.quiz_system.service.StudentResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/results")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentResultController {

    private final StudentResultService studentResultService;

    /**
     * Get all attempted quiz results
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ResultSummaryResponse>>> getMyResults() {

        List<ResultSummaryResponse> results =
                studentResultService.getMyResults();

        return ResponseEntity.ok(
                ApiResponse.<List<ResultSummaryResponse>>builder()
                        .success(true)
                        .message("Results fetched successfully.")
                        .data(results)
                        .errors(null)
                        .build()
        );
    }

    /**
     * Get detailed result of one quiz attempt
     */
    @GetMapping("/{attemptId}")
    public ResponseEntity<ApiResponse<ResultDetailResponse>> getResultDetails(
            @PathVariable Long attemptId
    ) {

        ResultDetailResponse response =
                studentResultService.getResultDetails(attemptId);

        return ResponseEntity.ok(
                ApiResponse.<ResultDetailResponse>builder()
                        .success(true)
                        .message("Result details fetched successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

}