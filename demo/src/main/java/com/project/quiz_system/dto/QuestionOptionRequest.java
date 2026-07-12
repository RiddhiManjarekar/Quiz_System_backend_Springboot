package com.project.quiz_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOptionRequest {

    @NotBlank(message = "Option text is required.")
    private String optionText;

    @NotNull(message = "Correct answer is required.")
    private Boolean correctAnswer;

}