package com.project.quiz_system.dto;

import lombok.Data;

@Data
public class RegisterRequest{
    private String name;
    private String email;
    private String phone;
    private  String password;
    private String role;
    private String education;
    private String grade;
    private String customGrade;
    private String department;
    private String qualification;
}