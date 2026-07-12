package com.project.quiz_system.repository;

import com.project.quiz_system.entity.Question;
import com.project.quiz_system.entity.QuestionOption;
import com.project.quiz_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionOptionRepository
        extends JpaRepository<QuestionOption, Long> {

    List<QuestionOption> findByQuestion(
            Question question
    );

    Optional<QuestionOption> findByIdAndQuestionQuizTeacher(
            Long id,
            User teacher
    );

    long countByQuestion(
            Question question
    );

    long countByQuestionAndCorrectAnswerTrue(
            Question question
    );

    boolean existsByQuestionAndOptionTextIgnoreCase(
            Question question,
            String optionText
    );

    boolean existsByQuestionAndOptionTextIgnoreCaseAndIdNot(
            Question question,
            String optionText,
            Long id
    );

    List<QuestionOption>
    findByIdIn(List<Long> ids);

    List<QuestionOption> findByQuestionAndIdIn(
            Question question,
            List<Long> ids
    );


}