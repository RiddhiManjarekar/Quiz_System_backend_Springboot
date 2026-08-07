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
import com.project.quiz_system.dto.ChangePasswordRequest;
import com.project.quiz_system.dto.ForgotPasswordRequest;
import com.project.quiz_system.dto.ResetPasswordRequest;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.MailException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final AuthenticatedUserService authenticatedUserService;

    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

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
                        new BadRequestException(
                                "Invalid email or password."
                        )
                );
        if (user.getStatus() != Status.ACTIVE) {
            throw new BadRequestException(
                    "Your account is inactive."
            );
        }

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName())
                .token(token)
                .build();
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = authenticatedUserService.getCurrentUser();
        if (
                !passwordEncoder.matches(
                        request.getCurrentPassword(),
                        user.getPassword()
                )
        ) {
            throw new BadRequestException(
                    "Current password is incorrect."
            );
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {

            throw new BadRequestException(
                    "New password and confirm password do not match."
            );
        }
        if (
                passwordEncoder.matches(
                        request.getNewPassword(),
                        user.getPassword()
                )
        ) {

            throw new BadRequestException(
                    "New password must be different from current password."
            );

        }
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );
        userRepository.save(user);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        // Never reveal whether the email exists
        if (user == null || user.getStatus() != Status.ACTIVE) {
            return;
        }
        if (
                user.getLastResetRequest() != null &&
                        user.getLastResetRequest()
                                .plusMinutes(2)
                                .isAfter(LocalDateTime.now())
        ) {

            return;
        }
        user.setLastResetRequest(LocalDateTime.now());


        String token = user.getResetPasswordToken();

        if (token == null ||
                user.getResetPasswordTokenExpiry() == null ||
                user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {

            token = UUID.randomUUID().toString();

            user.setResetPasswordToken(token);
            user.setResetPasswordTokenExpiry(
                    LocalDateTime.now().plusMinutes(30)
            );
        }
            String resetLink =
                    frontendUrl +
                            "/reset-password?token=" +
                            token;


            userRepository.save(user);
            try {

                emailService.sendPasswordResetEmail(
                        user.getEmail(),
                        user.getName(),
                        resetLink
                );

            } catch (MailException ex) {

                throw new BadRequestException(
                        "Unable to send email. Please try again later."
                );
            }

    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        User user = userRepository
                .findByResetPasswordToken(request.getToken())
                .orElseThrow(() ->
                        new BadRequestException(
                                "Invalid reset password token."
                        )
                );
        if(user.getStatus()!=Status.ACTIVE){

            throw new BadRequestException(
                    "Account is inactive."
            );

        }

        if (
                user.getResetPasswordTokenExpiry() == null ||
                        user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())
        ) {

            throw new BadRequestException(
                    "Reset password link has expired."
            );
        }

        if (!request.getNewPassword().equals(
                request.getConfirmPassword()
        )) {

            throw new BadRequestException(
                    "New password and confirm password do not match."
            );
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword()
        )) {

            throw new BadRequestException(
                    "New password must be different from the current password."
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        // Clear token after successful reset
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        user.setLastResetRequest(null);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void validatePasswordResetToken(String token) {

        User user = userRepository
                .findByResetPasswordToken(token)
                .orElseThrow(() ->
                        new BadRequestException("Invalid token."));

        if (user.getStatus() != Status.ACTIVE) {
            throw new BadRequestException(
                    "Account is inactive."
            );
        }
        if (
                user.getResetPasswordTokenExpiry() == null ||
                        user.getResetPasswordTokenExpiry()
                                .isBefore(LocalDateTime.now())
        ) {
            throw new BadRequestException(
                    "Reset link expired."
            );
        }
       
    }
}