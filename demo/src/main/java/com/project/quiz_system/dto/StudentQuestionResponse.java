package com.project.quiz_system.dto;

import lombok.*;
import java.util.List;

import com.project.quiz_system.enums.QuestionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentQuestionResponse {

    private Long id;

    private String questionText;

    private QuestionType questionType;

    private Double marks;

    private Integer displayOrder;

    private List<StudentOptionResponse> options;

}