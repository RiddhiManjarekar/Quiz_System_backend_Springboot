package com.project.quiz_system.service;

import com.project.quiz_system.dto.AuthResponse;
import com.project.quiz_system.dto.LoginRequest;
import com.project.quiz_system.dto.RegisterRequest;
import com.project.quiz_system.entity.Role;
import com.project.quiz_system.entity.User;
import com.project.quiz_system.enums.Education;
import com.project.quiz_system.enums.Status;
import com.project.quiz_system.repository.RoleRepository;
import com.project.quiz_system.repository.UserRepository;
import com.project.quiz_system.security.JwtService;
import lombok.RequiredArgsConstructor;
import com.project.quiz_system.exception.ResourceNotFoundException;
import com.project.quiz_system.exception.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class AuthService{
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists.");
        }

        Role role = roleRepository.findByRoleName(
                request.getRole().toUpperCase()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Role not found.")
        );

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(Status.ACTIVE)
                .education(
                        request.getEducation() != null
                                ? Education.valueOf(request.getEducation().toUpperCase())
                                : null
                )
                .grade(request.getGrade())
                .customGrade(request.getCustomGrade())
                .department(request.getDepartment())
                .qualification(request.getQualification())
                .role(role)
                .build();

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found."
                        )
                );

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName())
                .token(token)
                .build();
    }
}