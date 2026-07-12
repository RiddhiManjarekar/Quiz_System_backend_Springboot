package com.project.quiz_system.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswerRequest {

    private Long questionId;

    /*
     * SINGLE_CHOICE
     * MULTIPLE_CHOICE
     * TRUE_FALSE
     */
    private List<Long> selectedOptionIds;

    /*
     * DESCRIPTIVE
     */
    private String descriptiveAnswer;

}