package com.project.quiz_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardResponse {

    private Long totalStudents;

    private Long activeStudents;

    private Long inactiveStudents;

    private Long totalTeachers;

    private Long activeTeachers;

    private Long inactiveTeachers;

    private Long totalQuizzes;

    private Long draftQuizzes;

    private Long activeQuizzes;

    private Long inactiveQuizzes;

    private Long totalAttempts;
}