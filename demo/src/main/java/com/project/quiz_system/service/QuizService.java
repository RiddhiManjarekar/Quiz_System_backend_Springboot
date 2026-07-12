package com.project.quiz_system.service;

import com.project.quiz_system.dto.QuizRequest;
import com.project.quiz_system.dto.QuizResponse;
import com.project.quiz_system.dto.QuizStatusRequest;
import com.project.quiz_system.entity.Quiz;
import com.project.quiz_system.entity.User;
import com.project.quiz_system.enums.QuizStatus;
import com.project.quiz_system.mapper.QuizMapper;
import com.project.quiz_system.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import com.project.quiz_system.exception.*;
import org.springframework.transaction.annotation.Transactional;
import static com.project.quiz_system.util.EditQuizUtil.validateQuizEditable;

import java.util.List;


@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final QuizValidationService quizValidationService;
    

    private Quiz getTeacherQuiz(Long id){

        User teacher =
                authenticatedUserService.getCurrentUser();

        return quizRepository
                .findByIdAndTeacher(id,teacher)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Quiz not found."
                        ));

    }

    private void validateQuizRequest(QuizRequest request) {

        // TODO:
// Validate passing marks after total marks
// are calculated from questions.
        if (request.getPassingMarks() < 0) {
            throw new BadRequestException(
                    "Passing marks cannot be negative."
            );
        }

        if (request.getStartTime() != null &&
                request.getEndTime() != null) {

            if (!request.getStartTime()
                    .isBefore(request.getEndTime())) {

                throw new BadRequestException(
                        "Start time must be before end time."
                );
            }

            long minutes = Duration.between(
                    request.getStartTime(),
                    request.getEndTime()
            ).toMinutes();

            if (request.getDurationMinutes() > minutes) {

                throw new BadRequestException(
                        "Quiz duration exceeds scheduled time."
                );
            }

        }

    }

    @Transactional
    public QuizResponse createQuiz(QuizRequest request) {

        User teacher = authenticatedUserService.getCurrentUser();
        validateQuizRequest(request);
        Quiz quiz = quizMapper.toEntity(request, teacher);

        quiz.setStatus(QuizStatus.DRAFT);

        quiz.setTotalMarks(0);

        Quiz savedQuiz = quizRepository.save(quiz);

        return quizMapper.toResponse(savedQuiz);
    }

    public List<QuizResponse> getMyQuizzes() {

        User teacher = authenticatedUserService.getCurrentUser();

        return quizRepository.findByTeacher(teacher)
                .stream()
                .map(quizMapper::toResponse)
                .toList();
    }

    public QuizResponse getQuizById(Long id) {
        Quiz quiz = getTeacherQuiz(id);
        return quizMapper.toResponse(quiz);
    }

    @Transactional
    public QuizResponse updateQuiz(Long id,
                                   QuizRequest request) {

        Quiz quiz = getTeacherQuiz(id);
        validateQuizEditable(quiz);
        validateQuizRequest(request);
        quizMapper.updateEntity(quiz, request);
        Quiz updatedQuiz = quizRepository.save(quiz);
        return quizMapper.toResponse(updatedQuiz);
    }

    @Transactional
    public void deleteQuiz(Long id) {
        Quiz quiz = getTeacherQuiz(id);
        validateQuizEditable(quiz);
        quizRepository.delete(quiz);
    }

    @Transactional
    public QuizResponse updateQuizStatus(
            Long id,
            QuizStatusRequest request) {

        Quiz quiz = getTeacherQuiz(id);

        if (request.getStatus() == QuizStatus.ACTIVE) {
            quizValidationService.validateQuiz(quiz);
        }

        quiz.setStatus(request.getStatus());

        quizRepository.save(quiz);

        return quizMapper.toResponse(quiz);
    }
}