package com.project.quiz_system.entity;

import com.project.quiz_system.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id",nullable=false)
    private Quiz quiz;

    @Column(nullable = false,length = 2000)
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private QuestionType questionType;

    @Column(nullable = false)
    private Double marks;

    @Column(nullable = false)
    @Builder.Default
    private Double negativeMarks = 0.0;

    private Integer displayOrder;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<QuestionOption> options = new ArrayList<>();
}