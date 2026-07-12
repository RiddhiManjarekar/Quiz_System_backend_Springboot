package com.project.quiz_system.dto;

import com.project.quiz_system.enums.QuestionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {

    private Long id;

    private Long quizId;

    private String questionText;

    private QuestionType questionType;

    private Double marks;

    private Double negativeMarks;

    private Integer displayOrder;
}