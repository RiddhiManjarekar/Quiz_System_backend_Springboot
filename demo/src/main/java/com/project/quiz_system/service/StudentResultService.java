package com.project.quiz_system.service;

import com.project.quiz_system.dto.*;
import com.project.quiz_system.exception.BadRequestException;
import com.project.quiz_system.exception.ResourceNotFoundException;
import com.project.quiz_system.entity.*;
import com.project.quiz_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentResultService {

    private final QuizAttemptRepository quizAttemptRepository;

    private final StudentAnswerRepository studentAnswerRepository;

    private final StudentAnswerOptionRepository studentAnswerOptionRepository;

    private final AuthenticatedUserService authenticatedUserService;

    public List<ResultSummaryResponse> getMyResults() {

        User student = authenticatedUserService.getCurrentUser();

        List<QuizAttempt> attempts =
                quizAttemptRepository
                        .findByStudentOrderByUpdatedAtDesc(student);

        return attempts.stream()
                .map(attempt ->

                        ResultSummaryResponse.builder()
                                .attemptId(attempt.getId())
                                .quizId(attempt.getQuiz().getId())
                                .quizTitle(attempt.getQuiz().getTitle())
                                .score(attempt.getScore())
                                .percentage(attempt.getPercentage())
                                .passed(attempt.getPassed())
                                .status(attempt.getStatus())
                                .submittedAt(
                                        attempt.getSubmittedAt()
                                )
                                .build()

                )
                .toList();

    }

    public ResultDetailResponse getResultDetails(
            Long attemptId
    ) {

        User student =
                authenticatedUserService.getCurrentUser();

        QuizAttempt attempt =
                quizAttemptRepository
                        .findByIdAndStudent(
                                attemptId,
                                student
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Result not found."
                                ));

        List<StudentAnswer> answers =
                studentAnswerRepository
                        .findByAttempt(attempt);
        List<QuestionResultResponse> questionResponses =
                answers.stream()
                        .map(this::buildQuestionResult)
                        .toList();

        return ResultDetailResponse.builder()
                .attemptId(attempt.getId())
                .quizTitle(attempt.getQuiz().getTitle())
                .score(attempt.getScore())
                .percentage(attempt.getPercentage())
                .passed(attempt.getPassed())
                .questions(questionResponses)
                .build();
    }

    private QuestionResultResponse buildQuestionResult(
            StudentAnswer answer
    ) {

        Question question = answer.getQuestion();

        List<StudentAnswerOption> selectedOptions =
                studentAnswerOptionRepository
                        .findByStudentAnswer(answer);

        Set<Long> selectedIds =
                selectedOptions.stream()
                        .map(option ->
                                option.getQuestionOption().getId()
                        )
                        .collect(Collectors.toSet());

        List<OptionResultResponse> optionResponses =
                question.getOptions()
                        .stream()
                        .map(option ->

                                OptionResultResponse.builder()
                                        .id(option.getId())
                                        .optionText(option.getOptionText())
                                        .selected(
                                                selectedIds.contains(
                                                        option.getId()
                                                )
                                        )
                                        .correct(
                                                option.getCorrectAnswer()
                                        )
                                        .build()

                        )
                        .toList();

        return QuestionResultResponse.builder()
                .questionId(question.getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType())
                .marks(question.getMarks())
                .marksObtained(
                        answer.getMarksObtained()
                )
                .correct(
                        answer.getCorrect()
                )
                .descriptiveAnswer(
                        answer.getDescriptiveAnswer()
                )
                .options(optionResponses)
                .build();

    }
}
