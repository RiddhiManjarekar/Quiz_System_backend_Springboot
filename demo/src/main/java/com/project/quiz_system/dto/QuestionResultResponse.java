package com.project.quiz_system.dto;

import com.project.quiz_system.enums.QuestionType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResultResponse {

    private Long questionId;

    private String questionText;

    private QuestionType questionType;

    private Double marks;

    private Double marksObtained;

    private Boolean correct;

    private String descriptiveAnswer;

    private List<OptionResultResponse> options;

}