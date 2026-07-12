package com.project.quiz_system.mapper;

import com.project.quiz_system.dto.StudentResponse;
import com.project.quiz_system.entity.User;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentResponse toResponse(User user){

        return StudentResponse.builder()

                .id(user.getId())

                .name(user.getName())

                .email(user.getEmail())

                .phone(user.getPhone())

                .education(user.getEducation())

                .grade(user.getGrade())

                .customGrade(user.getCustomGrade())

                .status(user.getStatus())

                .build();
    }

}