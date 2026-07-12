package com.project.quiz_system.repository;

import com.project.quiz_system.entity.StudentAnswer;
import com.project.quiz_system.entity.QuizAttempt;
import com.project.quiz_system.entity.User;
import java.util.List;
import java.util.Optional;
import com.project.quiz_system.enums.QuestionType;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAnswerRepository
        extends JpaRepository<StudentAnswer,Long> {
     List<StudentAnswer> findByAttempt(QuizAttempt attempt);
     List<StudentAnswer> findByQuestionQuestionType(
             QuestionType questionType
     );
     Optional<StudentAnswer> findById(
             Long id
     );

     @Query("""
SELECT sa
FROM StudentAnswer sa
WHERE sa.question.questionType =
'DESCRIPTIVE'
AND sa.marksObtained IS NULL
AND sa.attempt.quiz.teacher = :teacher
""")
     List<StudentAnswer> findPendingDescriptiveAnswers(
             User teacher
     );
}