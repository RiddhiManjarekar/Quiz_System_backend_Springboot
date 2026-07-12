package com.project.quiz_system.service;

import com.project.quiz_system.dto.TeacherResponse;
import com.project.quiz_system.dto.UserStatusRequest;
import com.project.quiz_system.entity.Role;
import com.project.quiz_system.entity.User;
import com.project.quiz_system.mapper.TeacherMapper;
import com.project.quiz_system.repository.RoleRepository;
import com.project.quiz_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.project.quiz_system.exception.BadRequestException;
import com.project.quiz_system.exception.ResourceNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTeacherService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final TeacherMapper teacherMapper;

    private Role getTeacherRole(){

        return roleRepository
                .findByRoleName("TEACHER")
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Teacher role not found."
                        ));
    }

    private User getTeacher(Long id){

        return userRepository
                .findByIdAndRole(
                        id,
                        getTeacherRole()
                )
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Teacher not found."
                        ));
    }

    /*
     * GET ALL TEACHERS
     */
    public List<TeacherResponse> getTeachers(){

        return userRepository
                .findByRole(getTeacherRole())
                .stream()
                .map(teacherMapper::toResponse)
                .toList();
    }

    /*
     * GET TEACHER BY ID
     */
    public TeacherResponse getTeacherById(Long id){

        return teacherMapper.toResponse(
                getTeacher(id)
        );
    }

    /*
     * UPDATE STATUS
     */
    public TeacherResponse updateTeacherStatus(
            Long id,
            UserStatusRequest request
    ){

        User teacher = getTeacher(id);

        teacher.setStatus(request.getStatus());

        User updatedTeacher =
                userRepository.save(teacher);

        return teacherMapper.toResponse(
                updatedTeacher
        );
    }

    /*
     * DELETE TEACHER
     */
    public void deleteTeacher(Long id){

        User teacher = getTeacher(id);

        userRepository.delete(teacher);
    }

}