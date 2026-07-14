package com.project.quiz_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExistingAttemptResponse {

    private Boolean exists;

    private Long attemptId;

    private Long remainingSeconds;

}