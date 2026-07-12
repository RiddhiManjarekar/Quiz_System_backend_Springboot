package com.project.quiz_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_answer_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_answer_id", nullable = false)
    private StudentAnswer studentAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_option_id", nullable = false)
    private QuestionOption questionOption;
}