package com.project.quiz_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DescriptiveAnswerResponse {

    private Long studentAnswerId;

    private Long attemptId;

    private Long questionId;

    private String questionText;

    private Double questionMarks;

    private String studentName;

    private String answer;

    private Double marksObtained;

}