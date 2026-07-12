package com.project.quiz_system.util;

import com.project.quiz_system.entity.Quiz;
import com.project.quiz_system.enums.QuizStatus;
import com.project.quiz_system.exception.BadRequestException;

public final class EditQuizUtil {

    private EditQuizUtil() {
    }

    public static void validateQuizEditable(Quiz quiz) {

        if (quiz.getStatus() != QuizStatus.DRAFT) {
            throw new BadRequestException(
                    "Deactivate the quiz before making changes."
            );
        }
    }
}