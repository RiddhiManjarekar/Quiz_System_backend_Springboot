package com.project.quiz_system.dto;

import com.project.quiz_system.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusRequest {

    @NotNull
    private Status status;

}