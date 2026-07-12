package com.project.quiz_system.controller;

import com.project.quiz_system.common.ApiResponse;
import com.project.quiz_system.dto.QuestionOptionRequest;
import com.project.quiz_system.dto.QuestionOptionResponse;
import com.project.quiz_system.service.QuestionOptionService;
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
public class QuestionOptionController {

    private final QuestionOptionService optionService;

    @PostMapping("/questions/{questionId}/options")
    public ResponseEntity<ApiResponse<QuestionOptionResponse>> createOption(
            @PathVariable Long questionId,
            @Valid @RequestBody QuestionOptionRequest request
    ) {

        QuestionOptionResponse response =
                optionService.createOption(questionId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<QuestionOptionResponse>builder()
                                .success(true)
                                .message("Option created successfully.")
                                .data(response)
                                .errors(null)
                                .build()
                );
    }

    @GetMapping("/questions/{questionId}/options")
    public ResponseEntity<ApiResponse<List<QuestionOptionResponse>>> getOptions(
            @PathVariable Long questionId
    ) {

        List<QuestionOptionResponse> response =
                optionService.getOptions(questionId);

        return ResponseEntity.ok(
                ApiResponse.<List<QuestionOptionResponse>>builder()
                        .success(true)
                        .message("Options fetched successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    @GetMapping("/options/{optionId}")
    public ResponseEntity<ApiResponse<QuestionOptionResponse>> getOption(
            @PathVariable Long optionId
    ) {

        QuestionOptionResponse response =
                optionService.getOption(optionId);

        return ResponseEntity.ok(
                ApiResponse.<QuestionOptionResponse>builder()
                        .success(true)
                        .message("Option fetched successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    @PutMapping("/options/{optionId}")
    public ResponseEntity<ApiResponse<QuestionOptionResponse>> updateOption(
            @PathVariable Long optionId,
            @Valid @RequestBody QuestionOptionRequest request
    ) {

        QuestionOptionResponse response =
                optionService.updateOption(optionId, request);

        return ResponseEntity.ok(
                ApiResponse.<QuestionOptionResponse>builder()
                        .success(true)
                        .message("Option updated successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<ApiResponse<Void>> deleteOption(
            @PathVariable Long optionId
    ) {

        optionService.deleteOption(optionId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Option deleted successfully.")
                        .data(null)
                        .errors(null)
                        .build()
        );
    }

}