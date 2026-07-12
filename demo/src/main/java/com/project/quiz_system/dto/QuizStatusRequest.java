package com.project.quiz_system.dto;

import com.project.quiz_system.enums.QuizStatus;
import lombok.*;
import jakarta.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizStatusRequest {

    @NotNull
    private QuizStatus status;
}