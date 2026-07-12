package com.project.quiz_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOptionResponse {

    private Long id;

    private Long questionId;

    private String optionText;

    private Boolean correctAnswer;

}