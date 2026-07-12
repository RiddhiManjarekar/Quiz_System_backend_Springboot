package com.project.quiz_system.dto;

import lombok.*;
import java.util.List;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentQuizResponse {

    private Long id;

    private String title;

    private String description;

    private Integer durationMinutes;

    private Integer totalMarks;

    private Integer passingMarks;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<StudentQuestionResponse> questions;

}