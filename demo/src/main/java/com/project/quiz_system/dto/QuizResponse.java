package com.project.quiz_system.dto;

import com.project.quiz_system.enums.QuizStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {

    private Long id;

    private String title;

    private String description;

    private Integer durationMinutes;

    private Integer totalMarks;

    private Integer passingMarks;

    private QuizStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long teacherId;

    private String teacherName;
}