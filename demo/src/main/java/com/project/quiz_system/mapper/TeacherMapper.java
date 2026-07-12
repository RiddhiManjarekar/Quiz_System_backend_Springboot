package com.project.quiz_system.mapper;

import com.project.quiz_system.dto.TeacherResponse;
import com.project.quiz_system.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public TeacherResponse toResponse(User teacher){

        return TeacherResponse.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .email(teacher.getEmail())
                .phone(teacher.getPhone())
                .department(teacher.getDepartment())
                .qualification(teacher.getQualification())
                .status(teacher.getStatus())
                .build();
    }

}