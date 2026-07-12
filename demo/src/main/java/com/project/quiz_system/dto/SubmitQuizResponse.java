package com.project.quiz_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitQuizResponse {

    private Long attemptId;

    private Double score;

    private Double percentage;

    private Boolean passed;

    private String message;

}