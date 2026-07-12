package com.project.quiz_system.dto;

import lombok.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    @Positive
    private Integer durationMinutes;

    @NotNull
    @Positive
    private Integer passingMarks;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}