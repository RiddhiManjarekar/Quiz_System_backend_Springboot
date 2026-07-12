package com.project.quiz_system.service;

import com.project.quiz_system.dto.EvaluationResult;
import com.project.quiz_system.entity.Question;
import com.project.quiz_system.entity.QuestionOption;
import com.project.quiz_system.enums.QuestionType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EvaluationService {

    /**
     * Evaluates one question.
     */
    public EvaluationResult evaluateQuestion(
            Question question,
            List<QuestionOption> selectedOptions
    ) {

        return switch (question.getQuestionType()) {

            case SINGLE_CHOICE ->
                    evaluateSingleChoice(question, selectedOptions);

            case MULTIPLE_CHOICE ->
                    evaluateMultipleChoice(question, selectedOptions);

            case TRUE_FALSE ->
                    evaluateTrueFalse(question, selectedOptions);

            case DESCRIPTIVE ->
                    EvaluationResult.builder()
                            .correct(null)
                            .marksObtained(null)
                            .build();
        };

    }

    /**
     * SINGLE CHOICE
     */
    private EvaluationResult evaluateSingleChoice(
            Question question,
            List<QuestionOption> selected
    ) {

        if (selected.size() != 1) {

            return wrong(question);

        }

        boolean correct =
                selected.get(0).getCorrectAnswer();

        return correct
                ? correct(question)
                : wrong(question);

    }

    /**
     * TRUE/FALSE
     */
    private EvaluationResult evaluateTrueFalse(
            Question question,
            List<QuestionOption> selected
    ) {

        return evaluateSingleChoice(question, selected);

    }

    /**
     * MULTIPLE CHOICE
     */
    private EvaluationResult evaluateMultipleChoice(
            Question question,
            List<QuestionOption> selected
    ) {

        Set<Long> selectedIds =
                selected.stream()
                        .map(QuestionOption::getId)
                        .collect(Collectors.toSet());

        Set<Long> correctIds =
                question.getOptions()
                        .stream()
                        .filter(QuestionOption::getCorrectAnswer)
                        .map(QuestionOption::getId)
                        .collect(Collectors.toSet());

        boolean correct =
                selectedIds.equals(correctIds);

        return correct
                ? correct(question)
                : wrong(question);

    }

    /**
     * Correct answer.
     */
    private EvaluationResult correct(
            Question question
    ) {

        return EvaluationResult.builder()
                .correct(true)
                .marksObtained(
                        question.getMarks()
                )
                .build();

    }

    /**
     * Wrong answer.
     */
    private EvaluationResult wrong(
            Question question
    ) {

        Double negative = question.getNegativeMarks() == null
                ? 0.0
                : question.getNegativeMarks();

        return EvaluationResult.builder()
                .correct(false)
                .marksObtained(-negative)
                .build();

    }

    /**
     * Percentage
     */
    public Double calculatePercentage(
            Double score,
            Double totalMarks
    ) {

        if (totalMarks == 0) {

            return 0.0;

        }

        return (score / totalMarks) * 100;

    }

    /**
     * Pass / Fail
     */
    public Boolean isPassed(
            Double score,
            Double passingMarks
    ) {

        return score >= passingMarks;

    }

}