package com.project.quiz_system.repository;

import com.project.quiz_system.entity.Quiz;
import com.project.quiz_system.entity.QuizAttempt;
import com.project.quiz_system.entity.User;
import com.project.quiz_system.enums.AttemptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface QuizAttemptRepository
        extends JpaRepository<QuizAttempt, Long> {

    boolean existsByQuizAndStudent(
            Quiz quiz,
            User student
    );

    Optional<QuizAttempt> findByQuizAndStudent(
            Quiz quiz,
            User student
    );
    Optional<QuizAttempt> findByIdAndStudent(
            Long id,
            User student
    );

    List<QuizAttempt> findByStudentOrderByUpdatedAtDesc(
            User student
    );

    List<QuizAttempt> findByQuizTeacher(
            User teacher
    );

    Optional<QuizAttempt> findByQuizAndStudentAndStatus(
            Quiz quiz,
            User student,
            AttemptStatus status
    );
    boolean existsByQuizAndStudentAndStatus(
            Quiz quiz,
            User student,
            AttemptStatus status
    );

}