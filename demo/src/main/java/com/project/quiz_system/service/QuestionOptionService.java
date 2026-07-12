package com.project.quiz_system.service;

import com.project.quiz_system.dto.QuestionOptionRequest;
import com.project.quiz_system.dto.QuestionOptionResponse;
import com.project.quiz_system.entity.Question;
import com.project.quiz_system.entity.QuestionOption;
import com.project.quiz_system.entity.User;
import com.project.quiz_system.enums.QuestionType;
import com.project.quiz_system.enums.QuizStatus;
import com.project.quiz_system.exception.BadRequestException;
import com.project.quiz_system.exception.ResourceNotFoundException;
import com.project.quiz_system.mapper.QuestionOptionMapper;
import com.project.quiz_system.repository.QuestionOptionRepository;
import com.project.quiz_system.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.project.quiz_system.util.EditQuizUtil.validateQuizEditable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionOptionService {

    private final QuestionOptionRepository optionRepository;

    private final QuestionRepository questionRepository;

    private final QuestionOptionMapper optionMapper;

    private final AuthenticatedUserService authenticatedUserService;

    private static final int MAX_OPTIONS = 6;

    // =====================================================
    // PRIVATE METHODS
    // =====================================================

    private Question getTeacherQuestion(Long questionId) {

        User teacher = authenticatedUserService.getCurrentUser();

        return questionRepository
                .findByIdAndQuizTeacher(questionId, teacher)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Question not found."));
    }

    private QuestionOption getTeacherOption(Long optionId) {

        User teacher = authenticatedUserService.getCurrentUser();

        return optionRepository
                .findByIdAndQuestionQuizTeacher(optionId, teacher)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Option not found."));
    }

    private void validateCreate(
            Question question,
            QuestionOptionRequest request
    ) {

        if (question.getQuestionType() == QuestionType.DESCRIPTIVE) {

            throw new BadRequestException(
                    "Descriptive questions cannot have options."
            );
        }

        long optionCount =
                optionRepository.countByQuestion(question);

        if (optionCount >= MAX_OPTIONS) {

            throw new BadRequestException(
                    "Maximum 6 options are allowed."
            );
        }

        boolean exists =
                optionRepository.existsByQuestionAndOptionTextIgnoreCase(
                        question,
                        request.getOptionText()
                );

        if (exists) {

            throw new BadRequestException(
                    "Option already exists."
            );
        }

    }

    private void validateUpdate(
            QuestionOption option,
            QuestionOptionRequest request
    ) {

        boolean exists =
                optionRepository
                        .existsByQuestionAndOptionTextIgnoreCaseAndIdNot(
                                option.getQuestion(),
                                request.getOptionText(),
                                option.getId()
                        );

        if (exists) {

            throw new BadRequestException(
                    "Option already exists."
            );
        }

    }

    // =====================================================
    // CRUD
    // =====================================================

    @Transactional
    public QuestionOptionResponse createOption(
            Long questionId,
            QuestionOptionRequest request
    ) {

        Question question =
                getTeacherQuestion(questionId);

        validateQuizEditable(question.getQuiz());
        validateCreate(question, request);

        QuestionOption option =
                optionMapper.toEntity(request, question);

        QuestionOption saved =
                optionRepository.save(option);

        return optionMapper.toResponse(saved);
    }

    public List<QuestionOptionResponse> getOptions(
            Long questionId
    ) {

        Question question =
                getTeacherQuestion(questionId);

        return optionRepository.findByQuestion(question)
                .stream()
                .map(optionMapper::toResponse)
                .toList();
    }

    public QuestionOptionResponse getOption(
            Long optionId
    ) {

        QuestionOption option =
                getTeacherOption(optionId);

        return optionMapper.toResponse(option);
    }

    @Transactional
    public QuestionOptionResponse updateOption(
            Long optionId,
            QuestionOptionRequest request
    ) {

        QuestionOption option =
                getTeacherOption(optionId);
        validateQuizEditable(option.getQuestion().getQuiz());

        validateUpdate(option, request);

        optionMapper.updateEntity(option, request);

        QuestionOption updated =
                optionRepository.save(option);

        return optionMapper.toResponse(updated);
    }

    @Transactional
    public void deleteOption(
            Long optionId
    ) {

        QuestionOption option =
                getTeacherOption(optionId);

        validateQuizEditable(option.getQuestion().getQuiz());
        optionRepository.delete(option);
    }

}