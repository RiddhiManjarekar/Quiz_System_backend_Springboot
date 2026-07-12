package com.project.quiz_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionResultResponse {

    private Long id;

    private String optionText;

    private Boolean selected;

    private Boolean correct;

}