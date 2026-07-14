package com.project.quiz_system.dto;

import lombok.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswerRequest {

    @NotNull
    private Long questionId;

    /*
     * SINGLE_CHOICE
     * MULTIPLE_CHOICE
     * TRUE_FALSE
     */
    private List<@NotNull Long> selectedOptionIds;
    /*
     * DESCRIPTIVE
     */
    @Size(max=5000)
    private String descriptiveAnswer;

}