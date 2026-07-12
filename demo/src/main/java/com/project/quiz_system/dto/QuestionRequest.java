package com.project.quiz_system.dto;

import com.project.quiz_system.enums.QuestionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequest {

    @NotBlank(message = "Question text is required.")
    private String questionText;

    @NotNull(message = "Question type is required.")
    private QuestionType questionType;

    @NotNull(message = "Marks are required.")
    @Min(value = 1, message = "Marks must be greater than 0.")
    private Double marks;

    @Builder.Default
    private Double negativeMarks = 0.0;

    @NotNull(message = "Display order is required.")
    @Min(value = 1, message = "Display order must be greater than 0.")
    private Integer displayOrder;
}