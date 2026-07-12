package com.project.quiz_system.service;

import com.project.quiz_system.dto.*;
import com.project.quiz_system.exception.BadRequestException;
import com.project.quiz_system.exception.ResourceNotFoundException;
import com.project.quiz_system.entity.*;
import com.project.quiz_system.enums.*;
import com.project.quiz_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeacherEvaluationService {

    private final StudentAnswerRepository studentAnswerRepository;

    private final QuizAttemptRepository quizAttemptRepository;

    private final AuthenticatedUserService authenticatedUserService;
    private final EvaluationService evaluationService;


    public List<DescriptiveAnswerResponse>
    getPendingDescriptiveAnswers() {

        User teacher =
                authenticatedUserService.getCurrentUser();

        return studentAnswerRepository
                .findPendingDescriptiveAnswers(teacher)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public EvaluationResponse evaluateAnswer(
            Long studentAnswerId,
            EvaluationRequest request
    ){
        StudentAnswer answer =
                studentAnswerRepository
                        .findById(studentAnswerId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Answer not found."
                                )
                        );
        if (answer.getAttempt().getStatus()
                != AttemptStatus.SUBMITTED) {

            throw new BadRequestException(
                    "Quiz has not been submitted yet."
            );
        }
        if (answer.getQuestion().getQuestionType() !=
                QuestionType.DESCRIPTIVE) {

            throw new BadRequestException(
                    "Only descriptive answers can be evaluated."
            );
        }
        if (answer.getMarksObtained() != null) {

            throw new BadRequestException(
                    "Answer has already been evaluated."
            );
        }

        User teacher =
                authenticatedUserService.getCurrentUser();

        if (!answer.getAttempt()
                .getQuiz()
                .getTeacher()
                .getId()
                .equals(teacher.getId())) {

            throw new BadRequestException(
                    "Unauthorized evaluation."
            );
        }

        if (request.getMarksObtained() >
                answer.getQuestion().getMarks()) {

            throw new BadRequestException(
                    "Marks exceed question marks."
            );
        }
        if(request.getMarksObtained() < 0){

            throw new BadRequestException(
                    "Marks cannot be negative."
            );
        }
        answer.setMarksObtained(
                request.getMarksObtained()
        );

        answer.setCorrect(
                request.getMarksObtained() > 0
        );

        studentAnswerRepository.save(answer);

        recalculateAttempt(
                answer.getAttempt()
        );
        return EvaluationResponse.builder()
                .studentAnswerId(answer.getId())
                .marksObtained(answer.getMarksObtained())
                .message("Evaluation completed.")
                .build();
    }

    private DescriptiveAnswerResponse
    mapToResponse(StudentAnswer answer) {

        return DescriptiveAnswerResponse.builder()
                .studentAnswerId(answer.getId())
                .attemptId(
                        answer.getAttempt().getId()
                )
                .questionId(
                        answer.getQuestion().getId()
                )
                .questionText(
                        answer.getQuestion()
                                .getQuestionText()
                )
                .questionMarks(
                        answer.getQuestion()
                                .getMarks()
                )
                .studentName(
                        answer.getAttempt()
                                .getStudent()
                                .getName()
                )
                .answer(
                        answer.getDescriptiveAnswer()
                )
                .marksObtained(
                        answer.getMarksObtained()
                )
                .build();
    }

    private void recalculateAttempt(
            QuizAttempt attempt
    ){
        List<StudentAnswer> answers =
                studentAnswerRepository
                        .findByAttempt(attempt);
        double totalScore =
                answers.stream()
                        .filter(a ->
                                a.getMarksObtained() != null
                        )
                        .mapToDouble(
                                StudentAnswer::getMarksObtained
                        )
                        .sum();
        Quiz quiz = attempt.getQuiz();

        Double percentage =
                evaluationService.calculatePercentage(
                        totalScore,
                        quiz.getTotalMarks().doubleValue()
                );

        Boolean passed =
                evaluationService.isPassed(
                        totalScore,
                        quiz.getPassingMarks().doubleValue()
                );
        attempt.setScore(totalScore);

        attempt.setPercentage(
                percentage
        );

        attempt.setPassed(
                passed
        );

        quizAttemptRepository.save(
                attempt
        );
    }
}