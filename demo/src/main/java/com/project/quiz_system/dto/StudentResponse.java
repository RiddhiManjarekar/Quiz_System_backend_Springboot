package com.project.quiz_system.dto;

import com.project.quiz_system.enums.Education;
import com.project.quiz_system.enums.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private Education education;

    private String grade;

    private String customGrade;

    private Status status;
}