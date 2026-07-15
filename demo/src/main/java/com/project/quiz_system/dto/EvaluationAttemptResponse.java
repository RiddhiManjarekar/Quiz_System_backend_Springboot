package com.project.quiz_system.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationAttemptResponse {

    private Long attemptId;

    private String studentName;

    private String quizTitle;

    private Double totalMarks;

    private Double currentScore;

    private List<DescriptiveAnswerResponse> answers;

}