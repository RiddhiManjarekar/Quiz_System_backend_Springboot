package com.project.quiz_system.dto;

import com.project.quiz_system.enums.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherResponse {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String department;

    private String qualification;

    private Status status;
}