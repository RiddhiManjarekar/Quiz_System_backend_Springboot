package com.project.quiz_system.dto;

import lombok.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitQuizRequest {

    @NotNull
    @NotEmpty
    @Valid
    private List<StudentAnswerRequest> answers;

}