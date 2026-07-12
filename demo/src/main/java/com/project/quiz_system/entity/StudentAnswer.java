//@Entity
//@Table(name = "student_answers")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class StudentAnswer {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "attempt_id")
//    private QuizAttempt attempt;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "question_id")
//    private Question question;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "selected_option_id")
//    private QuestionOption selectedOption;
//
//    private Boolean correct;
//
//    private Double marksObtained;
//
//
//
//
//    private QuestionOption selectedOption;
//
//    @Column(columnDefinition = "TEXT")
//    private String descriptiveAnswer;
//
//    private Boolean correct;
//
//    private Double marksObtained;
//}

package com.project.quiz_system.entity;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private QuizAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(columnDefinition = "TEXT")
    private String descriptiveAnswer;

    private Boolean correct;

    private Double marksObtained;

    @OneToMany(
            mappedBy="studentAnswer",
            cascade=CascadeType.ALL,
            orphanRemoval=true
    )
    private List<StudentAnswerOption> selectedOptions =
            new ArrayList<>();
}