package com.project.quiz_system.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeAttemptResponse {

    private Long attemptId;

    private StudentQuizResponse quiz;

    private LocalDateTime startedAt;

    private Long remainingSeconds;

}