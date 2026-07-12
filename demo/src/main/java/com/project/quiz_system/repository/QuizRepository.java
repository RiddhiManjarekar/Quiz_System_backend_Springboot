package com.project.quiz_system.repository;

import com.project.quiz_system.entity.Quiz;
import com.project.quiz_system.entity.User;
import com.project.quiz_system.enums.QuizStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepository
        extends JpaRepository<Quiz, Long> {

    List<Quiz> findByTeacher(User teacher);

    Optional<Quiz> findByIdAndTeacher(
            Long id,
            User teacher
    );

    List<Quiz> findByStatus(QuizStatus status);

    Optional<Quiz> findByIdAndStatus(
            Long id,
            QuizStatus status
    );
    List<Quiz> findAllByOrderByCreatedAtDesc();

    List<Quiz> findByStatusOrderByCreatedAtDesc(
            QuizStatus status
    );

    long countByStatus(
            QuizStatus status
    );


}