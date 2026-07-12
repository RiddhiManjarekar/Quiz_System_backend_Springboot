package com.project.quiz_system.repository;

import com.project.quiz_system.entity.StudentAnswerOption;
import com.project.quiz_system.entity.StudentAnswer;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAnswerOptionRepository
        extends JpaRepository<StudentAnswerOption,Long> {

    List<StudentAnswerOption> findByStudentAnswer(StudentAnswer answer);
}