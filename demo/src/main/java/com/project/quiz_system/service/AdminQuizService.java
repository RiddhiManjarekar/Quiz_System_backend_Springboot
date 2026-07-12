package com.project.quiz_system.service;

import com.project.quiz_system.dto.QuizResponse;
import com.project.quiz_system.dto.QuizStatusRequest;
import com.project.quiz_system.entity.Quiz;
import com.project.quiz_system.enums.QuizStatus;
import com.project.quiz_system.mapper.QuizMapper;
import com.project.quiz_system.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.project.quiz_system.exception.BadRequestException;
import com.project.quiz_system.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminQuizService {

    private final QuizRepository quizRepository;

    private final QuizMapper quizMapper;

    private Quiz getQuiz(Long id) {

        return quizRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quiz not found."
                        ));
    }

    /*
     * GET ALL QUIZZES
     */
    public List<QuizResponse> getAllQuizzes() {

        return quizRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(quizMapper::toResponse)
                .toList();
    }

    /*
     * GET QUIZ BY ID
     */
    public QuizResponse getQuizById(Long id) {

        return quizMapper.toResponse(
                getQuiz(id)
        );
    }

    /*
     * GET QUIZZES BY STATUS
     */
    public List<QuizResponse> getQuizzesByStatus(
            QuizStatus status
    ) {

        return quizRepository
                .findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(quizMapper::toResponse)
                .toList();
    }

    /*
     * UPDATE QUIZ STATUS
     */
    public QuizResponse updateQuizStatus(
            Long id,
            QuizStatusRequest request
    ) {

        Quiz quiz = getQuiz(id);

        quiz.setStatus(request.getStatus());

        Quiz updatedQuiz =
                quizRepository.save(quiz);

        return quizMapper.toResponse(
                updatedQuiz
        );
    }

    /*
     * DELETE QUIZ
     */
    public void deleteQuiz(Long id) {

        Quiz quiz = getQuiz(id);

        quizRepository.delete(quiz);
    }

}