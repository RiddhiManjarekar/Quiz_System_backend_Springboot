package com.project.quiz_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationResult {

    private Boolean correct;

    private Double marksObtained;

}