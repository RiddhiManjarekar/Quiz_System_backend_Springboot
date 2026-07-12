
package com.project.quiz_system.service;

import com.project.quiz_system.dto.StudentResponse;
import com.project.quiz_system.dto.UserStatusRequest;
import com.project.quiz_system.entity.Role;
import com.project.quiz_system.entity.User;
import com.project.quiz_system.mapper.StudentMapper;
import com.project.quiz_system.repository.RoleRepository;
import com.project.quiz_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.project.quiz_system.exception.BadRequestException;
import com.project.quiz_system.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStudentService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final StudentMapper studentMapper;

    private Role getStudentRole() {

        return roleRepository
                .findByRoleName("STUDENT")
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student role not found."
                        )
                );
    }

    private User getStudent(Long id) {

        return userRepository
                .findByIdAndRole(
                        id,
                        getStudentRole()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found."
                        )
                );
    }

    /*
     * GET ALL STUDENTS
     */
    public List<StudentResponse> getStudents() {

        return userRepository
                .findByRole(getStudentRole())
                .stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    /*
     * GET STUDENT BY ID
     */
    public StudentResponse getStudentById(Long id) {

        return studentMapper.toResponse(
                getStudent(id)
        );
    }

    /*
     * UPDATE STATUS
     */
    public StudentResponse updateStudentStatus(
            Long id,
            UserStatusRequest request
    ) {

        User student = getStudent(id);

        student.setStatus(
                request.getStatus()
        );

        User updatedStudent =
                userRepository.save(student);

        return studentMapper.toResponse(
                updatedStudent
        );
    }

    /*
     * DELETE STUDENT
     */
    public void deleteStudent(Long id) {

        User student = getStudent(id);

        userRepository.delete(student);
    }

}