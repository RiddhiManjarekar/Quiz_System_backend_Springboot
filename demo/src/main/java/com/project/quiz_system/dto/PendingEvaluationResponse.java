package com.project.quiz_system.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingEvaluationResponse {

    private Long attemptId;

    private String studentName;

    private String quizTitle;

    private Integer pendingAnswers;

    private Double currentScore;
}