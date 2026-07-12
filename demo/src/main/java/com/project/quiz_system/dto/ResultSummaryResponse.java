package com.project.quiz_system.dto;

import com.project.quiz_system.enums.AttemptStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultSummaryResponse {

    private Long attemptId;

    private Long quizId;

    private String quizTitle;

    private Double score;

    private Double percentage;

    private Boolean passed;

    private AttemptStatus status;

    private LocalDateTime submittedAt;

}