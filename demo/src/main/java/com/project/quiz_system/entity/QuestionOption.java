package com.project.quiz_system.entity;

import com.project.quiz_system.enums.AttemptStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id",nullable=false)
    private Question question;

    @Column(nullable = false)
    private String optionText;

    @Column(nullable = false)
    private Boolean correctAnswer;


}