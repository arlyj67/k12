package com.schoolproject.k12.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "grade")
class Grade(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: School,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    val student: Student,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grading_criteria_id", nullable = false)
    val gradingCriteria: GradingCriteria,

    @Column(name = "written_works_score", nullable = false)
    val writtenWorksScore: Double,

    @Column(name = "performance_task_score", nullable = false)
    val performanceTaskScore: Double,

    @Column(name = "quarterly_assessment_score", nullable = false)
    val quarterlyAssessmentScore: Double,

    @Column(name = "initial_grade", nullable = false)
    val initialGrade: Double,

    @Column(name = "transmuted_grade", nullable = false)
    val transmutedGrade: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encoded_by", nullable = false)
    val encodedBy: Employee,

    @Column(name = "encoded_at", nullable = false)
    val encodedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

) {
    fun requireId(): UUID = id
        ?: error("Grade has not been persisted yet — id is null")

    @PrePersist
    fun onPrePersist() {
        val now = LocalDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun onPreUpdate() {
        updatedAt = LocalDateTime.now()
    }
}