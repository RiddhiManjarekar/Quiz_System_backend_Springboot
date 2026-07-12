package com.project.quiz_system.repository;

import com.project.quiz_system.entity.Question;
import com.project.quiz_system.entity.Quiz;
import com.project.quiz_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository
        extends JpaRepository<Question, Long> {

    List<Question> findByQuiz(Quiz quiz);

    Optional<Question> findByIdAndQuizTeacher(
            Long id,
            User teacher
    );



    long countByQuiz(Quiz quiz);

    boolean existsByQuizAndDisplayOrder(
            Quiz quiz,
            Integer displayOrder
    );
}