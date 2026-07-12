package com.project.quiz_system.dto;

import com.project.quiz_system.enums.AttemptStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartQuizResponse {

    private Long attemptId;

    private Long quizId;

    private String quizTitle;

    private Integer durationMinutes;

    private AttemptStatus status;

    private String message;
}