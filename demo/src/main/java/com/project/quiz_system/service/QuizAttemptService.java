package com.project.quiz_system.service;

import com.project.quiz_system.dto.StartQuizResponse;
import com.project.quiz_system.entity.Quiz;
import com.project.quiz_system.entity.QuizAttempt;
import com.project.quiz_system.entity.User;
import com.project.quiz_system.enums.AttemptStatus;
import com.project.quiz_system.enums.QuizStatus;
import com.project.quiz_system.mapper.StudentQuizMapper;
import com.project.quiz_system.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.project.quiz_system.exception.BadRequestException;
import com.project.quiz_system.exception.ResourceNotFoundException;

import com.project.quiz_system.dto.*;

import com.project.quiz_system.entity.*;

import com.project.quiz_system.enums.QuestionType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class QuizAttemptService {

    private final QuizRepository quizRepository;

    private final QuizAttemptRepository quizAttemptRepository;

    private final AuthenticatedUserService authenticatedUserService;

    private final QuestionRepository questionRepository;

    private final QuestionOptionRepository questionOptionRepository;

    private final StudentAnswerRepository studentAnswerRepository;

    private final StudentAnswerOptionRepository studentAnswerOptionRepository;

    private final EvaluationService evaluationService;

    private final StudentQuizMapper studentQuizMapper;

    public StartQuizResponse startQuiz(Long quizId) {

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

        QuizAttempt previous =
                quizAttemptRepository
                        .findByQuizAndStudent(quiz, student)
                        .orElse(null);

        if(previous != null){

            if(previous.getStatus()==AttemptStatus.SUBMITTED){
                throw new BadRequestException(
                        "You have already attempted this quiz."
                );
            }

            return StartQuizResponse.builder()
                    .attemptId(previous.getId())
                    .quizId(quiz.getId())
                    .quizTitle(quiz.getTitle())
                    .durationMinutes(quiz.getDurationMinutes())
                    .status(previous.getStatus())
                    .message("Quiz already in progress.")
                    .build();
        }


        QuizAttempt attempt = QuizAttempt.builder()
                .quiz(quiz)
                .student(student)
                .score(0.0)
                .percentage(0.0)
                .passed(false)
                .status(AttemptStatus.IN_PROGRESS)
                .build();

        QuizAttempt savedAttempt =
                quizAttemptRepository.save(attempt);

        return StartQuizResponse.builder()
                .attemptId(savedAttempt.getId())
                .quizId(quiz.getId())
                .quizTitle(quiz.getTitle())
                .durationMinutes(
                        quiz.getDurationMinutes()
                )
                .status(savedAttempt.getStatus())
                .message("Quiz started successfully.")
                .build();

    }

    public ExistingAttemptResponse checkExistingAttempt(
            Long quizId
    ){

        User student =
                authenticatedUserService.getCurrentUser();

        Quiz quiz =
                quizRepository.findById(quizId)
                        .orElseThrow(()->
                                new ResourceNotFoundException(
                                        "Quiz not found."
                                ));

        QuizAttempt attempt =
                quizAttemptRepository
                        .findByQuizAndStudentAndStatus(
                                quiz,
                                student,
                                AttemptStatus.IN_PROGRESS
                        )
                        .orElse(null);

        if(attempt==null){

            return ExistingAttemptResponse.builder()
                    .exists(false)
                    .attemptId(null)
                    .build();

        }

        return ExistingAttemptResponse.builder()
                .exists(true)
                .attemptId(attempt.getId())
                .build();

    }

    public ResumeAttemptResponse resumeAttempt(
            Long attemptId
    ){

        User student =
                authenticatedUserService.getCurrentUser();

        QuizAttempt attempt =
                quizAttemptRepository
                        .findByIdAndStudent(
                                attemptId,
                                student
                        )
                        .orElseThrow(()->
                                new ResourceNotFoundException(
                                        "Attempt not found."
                                ));

        if(attempt.getStatus()!=AttemptStatus.IN_PROGRESS){

            throw new BadRequestException(
                    "Quiz already submitted."
            );

        }

        Quiz quiz = attempt.getQuiz();
        if(
                quiz.getEndTime()!=null &&
                        LocalDateTime.now().isAfter(quiz.getEndTime())
        ){

            throw new BadRequestException(
                    "Quiz has already ended."
            );

        }

        long remainingSeconds =
                quiz.getDurationMinutes()*60L
                        -
                        java.time.Duration.between(
                                attempt.getStartedAt(),
                                LocalDateTime.now()
                        ).getSeconds();

        if (remainingSeconds <= 0) {

            throw new BadRequestException(
                    "Quiz time has expired."
            );

        }

        return ResumeAttemptResponse.builder()
                .attemptId(attempt.getId())
                .quiz(
                        studentQuizMapper.toResponse(quiz)
                )
                .remainingSeconds(remainingSeconds)
                .build();

    }

    @Transactional
    public SubmitQuizResponse submitQuiz(
            Long attemptId,
            SubmitQuizRequest request
    ) {

        User student = authenticatedUserService.getCurrentUser();

        QuizAttempt attempt = quizAttemptRepository
                .findByIdAndStudent(attemptId, student)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quiz attempt not found."
                        ));

        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new BadRequestException(
                    "Quiz has already been submitted."
            );
        }

        double totalScore = 0.0;

        Set<Long> answeredQuestions = new HashSet<>();

        for (StudentAnswerRequest answerRequest : request.getAnswers()) {
            if (!answeredQuestions.add(answerRequest.getQuestionId())) {

                throw new BadRequestException(
                        "Duplicate question submitted."
                );

            }
            totalScore += saveStudentAnswer(
                    attempt,
                    answerRequest
            );

        }

        updateAttemptResult(
                attempt,
                totalScore
        );

        return SubmitQuizResponse.builder()
                .attemptId(attempt.getId())
                .score(attempt.getScore())
                .percentage(attempt.getPercentage())
                .passed(attempt.getPassed())
                .message("Quiz submitted successfully.")
                .build();
    }

    private Double saveStudentAnswer(
            QuizAttempt attempt,
            StudentAnswerRequest request
    ) {

        Question question = questionRepository
                .findById(request.getQuestionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Question not found."
                        ));
        if (!question.getQuiz().getId().equals(attempt.getQuiz().getId())) {

            throw new BadRequestException(
                    "Question does not belong to this quiz."
            );

        }

        StudentAnswer studentAnswer = StudentAnswer.builder()
                .attempt(attempt)
                .question(question)
                .descriptiveAnswer(

                        request.getDescriptiveAnswer()
                )
                .build();

        studentAnswer = studentAnswerRepository.save(studentAnswer);

        /*
         * DESCRIPTIVE
         */
        if (question.getQuestionType() == QuestionType.DESCRIPTIVE) {

            if(request.getSelectedOptionIds()!=null){

                throw new BadRequestException(
                        "Descriptive question cannot have selected options."
                );
            }
            studentAnswer.setCorrect(null);
            studentAnswer.setMarksObtained(null);

            studentAnswerRepository.save(studentAnswer);

            return 0.0;
        }

        /*
         * Objective Questions
         */

        if(question.getQuestionType()!=QuestionType.DESCRIPTIVE
                &&
                request.getDescriptiveAnswer()!=null){

            throw new BadRequestException(
                    "Objective question cannot have descriptive answer."
            );

        }

        List<Long> optionIds =
                request.getSelectedOptionIds();

        if (optionIds == null || optionIds.isEmpty()) {

            EvaluationResult result =
                    evaluationService.evaluateQuestion(
                            question,
                            List.of()
                    );

            studentAnswer.setCorrect(
                    result.getCorrect()
            );

            studentAnswer.setMarksObtained(
                    result.getMarksObtained()
            );

            studentAnswerRepository.save(studentAnswer);

            return result.getMarksObtained();
        }

        List<QuestionOption> selectedOptions =
                questionOptionRepository.findByQuestionAndIdIn(
                        question,
                        optionIds
                );
        if (selectedOptions.size() != optionIds.size()) {
            throw new BadRequestException(
                    "One or more selected options are invalid."
            );
        }

        EvaluationResult result =
                evaluationService.evaluateQuestion(
                        question,
                        selectedOptions
                );

        studentAnswer.setCorrect(
                result.getCorrect()
        );

        studentAnswer.setMarksObtained(
                result.getMarksObtained()
        );

        studentAnswerRepository.save(studentAnswer);

        saveSelectedOptions(
                studentAnswer,
                selectedOptions
        );

        return result.getMarksObtained();
    }

    private void saveSelectedOptions(
            StudentAnswer studentAnswer,
            List<QuestionOption> selectedOptions
    ) {

        List<StudentAnswerOption> answerOptions =
                selectedOptions
                        .stream()
                        .map(option ->
                                StudentAnswerOption.builder()
                                        .studentAnswer(studentAnswer)
                                        .questionOption(option)
                                        .build()
                        )
                        .toList();

        studentAnswerOptionRepository
                .saveAll(answerOptions);

    }

    private void updateAttemptResult(
            QuizAttempt attempt,
            Double score
    ) {

        Quiz quiz = attempt.getQuiz();

        Double percentage =
                evaluationService.calculatePercentage(
                        score,
                        quiz.getTotalMarks().doubleValue()
                );

        Boolean passed =
                evaluationService.isPassed(
                        score,
                        quiz.getPassingMarks().doubleValue()
                );

        attempt.setScore(score);

        attempt.setPercentage(percentage);

        attempt.setPassed(passed);
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setStatus(
                AttemptStatus.SUBMITTED
        );

        quizAttemptRepository.save(attempt);

    }

}