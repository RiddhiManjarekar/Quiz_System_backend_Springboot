package com.project.quiz_system.controller;

import com.project.quiz_system.common.ApiResponse;
import com.project.quiz_system.dto.TeacherResponse;
import com.project.quiz_system.dto.UserStatusRequest;
import com.project.quiz_system.service.AdminTeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/teachers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTeacherController {

    private final AdminTeacherService adminTeacherService;

    /*
     * GET ALL TEACHERS
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherResponse>>> getTeachers(){

        List<TeacherResponse> teachers =
                adminTeacherService.getTeachers();

        return ResponseEntity.ok(
                ApiResponse.<List<TeacherResponse>>builder()
                        .success(true)
                        .message("Teachers fetched successfully.")
                        .data(teachers)
                        .errors(null)
                        .build()
        );
    }

    /*
     * GET TEACHER BY ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> getTeacherById(
            @PathVariable Long id){

        TeacherResponse response =
                adminTeacherService.getTeacherById(id);

        return ResponseEntity.ok(
                ApiResponse.<TeacherResponse>builder()
                        .success(true)
                        .message("Teacher fetched successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    /*
     * UPDATE STATUS
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TeacherResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UserStatusRequest request){

        TeacherResponse response =
                adminTeacherService.updateTeacherStatus(
                        id,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.<TeacherResponse>builder()
                        .success(true)
                        .message("Teacher status updated successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    /*
     * DELETE TEACHER
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(
            @PathVariable Long id){

        adminTeacherService.deleteTeacher(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Teacher deleted successfully.")
                        .data(null)
                        .errors(null)
                        .build()
        );
    }

}