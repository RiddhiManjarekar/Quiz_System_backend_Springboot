package com.project.quiz_system.service;

import com.project.quiz_system.dto.StudentQuizResponse;
import com.project.quiz_system.entity.Quiz;
import com.project.quiz_system.entity.User;
import com.project.quiz_system.enums.QuizStatus;
import com.project.quiz_system.mapper.StudentQuizMapper;
import com.project.quiz_system.repository.QuizAttemptRepository;
import com.project.quiz_system.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.project.quiz_system.exception.BadRequestException;
import com.project.quiz_system.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentQuizService {

    private final QuizRepository quizRepository;

    private final QuizAttemptRepository quizAttemptRepository;

    private final StudentQuizMapper studentQuizMapper;

    private final AuthenticatedUserService authenticatedUserService;

    /**
     * Returns quizzes available for current student.
     */
    public List<StudentQuizResponse> getAvailableQuizzes() {

        User student =
                authenticatedUserService.getCurrentUser();

        LocalDateTime now = LocalDateTime.now();

        return quizRepository.findByStatus(QuizStatus.ACTIVE)
                .stream()

                // Quiz not expired
                .filter(quiz ->
                        quiz.getStartTime() == null
                                || !now.isBefore(quiz.getStartTime())
                )

                .filter(quiz ->
                        quiz.getEndTime() == null
                                || !now.isAfter(quiz.getEndTime())
                )

                // Student hasn't attempted
                .filter(quiz ->
                        !quizAttemptRepository.existsByQuizAndStudentAndStatus(
                                quiz,
                                student,
                                AttemptStatus.SUBMITTED
                        )
                )

                .map(studentQuizMapper::toResponse)

                .toList();
    }

    /**
     * Returns one quiz for attempt.
     */
    public StudentQuizResponse getQuiz(Long quizId) {

        User student =
                authenticatedUserService.getCurrentUser();

        Quiz quiz = quizRepository
                .findByIdAndStatus(
                        quizId,
                        QuizStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quiz not found."
                        )
                );

        LocalDateTime now = LocalDateTime.now();

        if (quiz.getStartTime() != null &&
                now.isBefore(quiz.getStartTime())) {

            throw new BadRequestException(
                    "Quiz has not started yet."
            );
        }

        if (quiz.getEndTime() != null &&
                now.isAfter(quiz.getEndTime())) {

            throw new BadRequestException(
                    "Quiz has already ended."
            );
        }

        QuizAttempt attempt =
                quizAttemptRepository
                        .findByQuizAndStudent(quiz, student)
                        .orElse(null);

        if (attempt != null &&
                attempt.getStatus() == AttemptStatus.SUBMITTED) {

            throw new BadRequestException(
                    "You have already attempted this quiz."
            );
        }

        return studentQuizMapper.toResponse(quiz);

    }

}