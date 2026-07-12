package com.project.quiz_system.service;

import com.project.quiz_system.dto.QuestionRequest;
import com.project.quiz_system.dto.QuestionResponse;
import com.project.quiz_system.entity.Question;
import com.project.quiz_system.entity.Quiz;
import com.project.quiz_system.entity.User;
import com.project.quiz_system.enums.QuizStatus;
import com.project.quiz_system.exception.BadRequestException;
import com.project.quiz_system.exception.ResourceNotFoundException;
import com.project.quiz_system.mapper.QuestionMapper;
import com.project.quiz_system.repository.QuestionRepository;
import com.project.quiz_system.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.project.quiz_system.util.EditQuizUtil.validateQuizEditable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    private final QuizRepository quizRepository;

    private final QuestionMapper questionMapper;

    private final AuthenticatedUserService authenticatedUserService;

    /**
     * -----------------------------------------
     * PRIVATE METHODS
     * -----------------------------------------
     */

    private Quiz getTeacherQuiz(Long quizId) {

        User teacher = authenticatedUserService.getCurrentUser();

        return quizRepository
                .findByIdAndTeacher(quizId, teacher)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quiz not found."
                        ));
    }

    private Question getTeacherQuestion(Long questionId) {

        User teacher = authenticatedUserService.getCurrentUser();

        return questionRepository
                .findByIdAndQuizTeacher(questionId, teacher)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Question not found."
                        ));
    }

    private void validateQuestionRequest(
            QuestionRequest request
    ) {

        if (request.getMarks() <= 0) {

            throw new BadRequestException(
                    "Marks must be greater than zero."
            );
        }

        if (request.getNegativeMarks() != null &&
                request.getNegativeMarks() < 0) {

            throw new BadRequestException(
                    "Negative marks cannot be negative."
            );
        }

        if (request.getDisplayOrder() != null &&
                request.getDisplayOrder() <= 0) {

            throw new BadRequestException(
                    "Display order must be greater than zero."
            );
        }

    }

    private void updateQuizTotalMarks(Quiz quiz) {

        Double totalMarks = questionRepository
                .findByQuiz(quiz)
                .stream()
                .mapToDouble(Question::getMarks)
                .sum();

        quiz.setTotalMarks(totalMarks.intValue());

        quizRepository.save(quiz);

    }

    /**
     * -----------------------------------------
     * CRUD
     * -----------------------------------------
     */

    @Transactional
    public QuestionResponse createQuestion(
            Long quizId,
            QuestionRequest request
    ) {

        Quiz quiz = getTeacherQuiz(quizId);
        validateQuizEditable(quiz);

        validateQuestionRequest(request);

        Question question =
                questionMapper.toEntity(request, quiz);

        Question savedQuestion =
                questionRepository.save(question);

        updateQuizTotalMarks(quiz);

        return questionMapper.toResponse(savedQuestion);

    }

    public List<QuestionResponse> getQuestionsByQuiz(
            Long quizId
    ) {

        Quiz quiz = getTeacherQuiz(quizId);

        return questionRepository
                .findByQuiz(quiz)
                .stream()
                .map(questionMapper::toResponse)
                .toList();

    }

    public QuestionResponse getQuestionById(
            Long questionId
    ) {

        Question question =
                getTeacherQuestion(questionId);

        return questionMapper.toResponse(question);

    }

    @Transactional
    public QuestionResponse updateQuestion(
            Long questionId,
            QuestionRequest request
    ) {

        Question question =
                getTeacherQuestion(questionId);
        validateQuizEditable(question.getQuiz());
        validateQuestionRequest(request);

        questionMapper.updateEntity(
                question,
                request
        );

        Question updatedQuestion =
                questionRepository.save(question);

        updateQuizTotalMarks(question.getQuiz());

        return questionMapper.toResponse(updatedQuestion);

    }

    @Transactional
    public void deleteQuestion(
            Long questionId
    ) {

        Question question =
                getTeacherQuestion(questionId);

        Quiz quiz = question.getQuiz();

        validateQuizEditable(quiz);

        questionRepository.delete(question);

        updateQuizTotalMarks(quiz);

    }

}