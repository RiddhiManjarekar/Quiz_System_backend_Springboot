package com.project.quiz_system.service;

import com.project.quiz_system.entity.Question;
import com.project.quiz_system.entity.QuestionOption;
import com.project.quiz_system.entity.Quiz;
import com.project.quiz_system.enums.QuestionType;
import com.project.quiz_system.enums.QuizStatus;
import com.project.quiz_system.exception.BadRequestException;
import com.project.quiz_system.repository.QuestionOptionRepository;
import com.project.quiz_system.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuizValidationService {

    private final QuestionRepository questionRepository;

    private final QuestionOptionRepository optionRepository;

    public void validateQuiz(Quiz quiz) {

        validateQuestions(quiz);

        validateDisplayOrder(quiz);

        validatePassingMarks(quiz);

    }


    private void validateQuestions(
            Quiz quiz
    ) {

        List<Question> questions =
                questionRepository.findByQuiz(quiz);

        if (questions.isEmpty()) {

            throw new BadRequestException(
                    "Quiz must contain at least one question."
            );

        }

        for (Question question : questions) {

            validateQuestion(question);

        }

    }

    private void validateQuestion(
            Question question
    ) {

        List<QuestionOption> options =
                optionRepository.findByQuestion(question);

        switch (question.getQuestionType()) {

            case DESCRIPTIVE -> validateDescriptive(options);

            case SINGLE_CHOICE -> validateSingleChoice(options);

            case MULTIPLE_CHOICE -> validateMultipleChoice(options);

            case TRUE_FALSE -> validateTrueFalse(options);

        }

    }

    private void validateDescriptive(
            List<QuestionOption> options
    ) {

        if (!options.isEmpty()) {

            throw new BadRequestException(
                    "Descriptive questions cannot have options."
            );

        }

    }

    private void validateSingleChoice(
            List<QuestionOption> options
    ) {

        if (options.size() < 2) {

            throw new BadRequestException(
                    "Single choice question must have at least two options."
            );

        }

        long correct =
                options.stream()
                        .filter(QuestionOption::getCorrectAnswer)
                        .count();

        if (correct != 1) {

            throw new BadRequestException(
                    "Single choice question must have exactly one correct answer."
            );

        }

    }

    private void validateMultipleChoice(
            List<QuestionOption> options
    ) {

        if (options.size() < 2) {

            throw new BadRequestException(
                    "Multiple choice question must have at least two options."
            );

        }

        long correct =
                options.stream()
                        .filter(QuestionOption::getCorrectAnswer)
                        .count();

        if (correct < 1) {

            throw new BadRequestException(
                    "Multiple choice question must have at least one correct answer."
            );

        }

    }

    private void validateTrueFalse(
            List<QuestionOption> options
    ) {

        if (options.size() != 2) {

            throw new BadRequestException(
                    "True/False question must have exactly two options."
            );

        }

        Set<String> values = new HashSet<>();

        for (QuestionOption option : options) {

            String value =
                    option.getOptionText()
                            .trim()
                            .toLowerCase();

            values.add(value);

        }

        if (!values.contains("true") ||
                !values.contains("false")) {

            throw new BadRequestException(
                    "True/False question must contain True and False."
            );

        }

        long correct =
                options.stream()
                        .filter(QuestionOption::getCorrectAnswer)
                        .count();

        if (correct != 1) {

            throw new BadRequestException(
                    "True/False question must have exactly one correct answer."
            );

        }

    }

    private void validateDisplayOrder(
            Quiz quiz
    ) {


        List<Question> questions =
                questionRepository.findByQuiz(quiz);

        Set<Integer> displayOrders =
                new HashSet<>();

        for (Question question : questions) {

            Integer order = question.getDisplayOrder();

            if (order == null) {
                throw new BadRequestException(
                        "Display order is required."
                );
            }

            if (!displayOrders.add(
                    question.getDisplayOrder())) {

                throw new BadRequestException(
                        "Duplicate display order found."
                );

            }

        }

    }


    private void validatePassingMarks(
            Quiz quiz
    ) {

        if (quiz.getTotalMarks() <= 0) {

            throw new BadRequestException(
                    "Quiz total marks must be greater than zero."
            );

        }

        if (quiz.getPassingMarks() >
                quiz.getTotalMarks()) {

            throw new BadRequestException(
                    "Passing marks cannot exceed total marks."
            );

        }

    }

}