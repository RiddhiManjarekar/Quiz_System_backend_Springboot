package com.project.quiz_system.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultDetailResponse {

    private Long attemptId;

    private String quizTitle;

    private Double score;

    private Double percentage;

    private Boolean passed;

    private List<QuestionResultResponse> questions;

}