package com.project.quiz_system.controller;

import com.project.quiz_system.common.ApiResponse;
import com.project.quiz_system.dto.StudentResponse;
import com.project.quiz_system.dto.UserStatusRequest;
import com.project.quiz_system.service.AdminStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminStudentController {

    private final AdminStudentService adminStudentService;

    /*
     * GET ALL STUDENTS
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>>
    getStudents() {

        List<StudentResponse> students =
                adminStudentService.getStudents();

        return ResponseEntity.ok(
                ApiResponse.<List<StudentResponse>>builder()
                        .success(true)
                        .message("Students fetched successfully.")
                        .data(students)
                        .errors(null)
                        .build()
        );
    }

    /*
     * GET STUDENT BY ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>>
    getStudentById(
            @PathVariable Long id
    ) {

        StudentResponse response =
                adminStudentService.getStudentById(id);

        return ResponseEntity.ok(
                ApiResponse.<StudentResponse>builder()
                        .success(true)
                        .message("Student fetched successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    /*
     * UPDATE STATUS
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<StudentResponse>>
    updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UserStatusRequest request
    ) {

        StudentResponse response =
                adminStudentService
                        .updateStudentStatus(
                                id,
                                request
                        );

        return ResponseEntity.ok(
                ApiResponse.<StudentResponse>builder()
                        .success(true)
                        .message("Student status updated successfully.")
                        .data(response)
                        .errors(null)
                        .build()
        );
    }

    /*
     * DELETE STUDENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>>
    deleteStudent(
            @PathVariable Long id
    ) {

        adminStudentService.deleteStudent(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Student deleted successfully.")
                        .data(null)
                        .errors(null)
                        .build()
        );
    }

}