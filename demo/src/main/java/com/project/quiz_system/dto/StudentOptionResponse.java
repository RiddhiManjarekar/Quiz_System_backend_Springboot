package com.project.quiz_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentOptionResponse {

    private Long id;

    private String optionText;

}