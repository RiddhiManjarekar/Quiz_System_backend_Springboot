package com.project.quiz_system.service;

import com.project.quiz_system.dto.AdminDashboardResponse;
import com.project.quiz_system.entity.Role;
import com.project.quiz_system.enums.QuizStatus;
import com.project.quiz_system.exception.ResourceNotFoundException;
import com.project.quiz_system.enums.Status;
import com.project.quiz_system.repository.QuizAttemptRepository;
import com.project.quiz_system.repository.QuizRepository;
import com.project.quiz_system.repository.RoleRepository;
import com.project.quiz_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final QuizRepository quizRepository;

    private final QuizAttemptRepository quizAttemptRepository;

    public AdminDashboardResponse getDashboard() {

        Role studentRole = roleRepository
                .findByRoleName("STUDENT")
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student role not found."
                        ));

        Role teacherRole = roleRepository
                .findByRoleName("TEACHER")
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher role not found."
                        ));

        return AdminDashboardResponse.builder()

                .totalStudents(
                        userRepository.countByRole(studentRole)
                )

                .activeStudents(
                        userRepository.countByRoleAndStatus(
                                studentRole,
                                Status.ACTIVE
                        )
                )

                .inactiveStudents(
                        userRepository.countByRoleAndStatus(
                                studentRole,
                                Status.INACTIVE
                        )
                )

                .totalTeachers(
                        userRepository.countByRole(teacherRole)
                )

                .activeTeachers(
                        userRepository.countByRoleAndStatus(
                                teacherRole,
                                Status.ACTIVE
                        )
                )

                .inactiveTeachers(
                        userRepository.countByRoleAndStatus(
                                teacherRole,
                                Status.INACTIVE
                        )
                )

                .totalQuizzes(
                        quizRepository.count()
                )

                .draftQuizzes(
                        quizRepository.countByStatus(
                                QuizStatus.DRAFT
                        )
                )

                .activeQuizzes(
                        quizRepository.countByStatus(
                                QuizStatus.ACTIVE
                        )
                )

                .inactiveQuizzes(
                        quizRepository.countByStatus(
                                QuizStatus.INACTIVE
                        )
                )

                .totalAttempts(
                        quizAttemptRepository.count()
                )

                .build();
    }
}