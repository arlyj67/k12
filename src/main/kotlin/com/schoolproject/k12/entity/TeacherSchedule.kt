package com.schoolproject.k12.entity

import com.schoolproject.k12.model.DayOfWeek
import com.schoolproject.k12.model.ScheduleStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
import java.time.LocalTime
import java.util.UUID

@Entity
@Table(name = "teacher_schedule")
class TeacherSchedule(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    // Let JPA own ID generation — no Kotlin-level default needed
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    val employee: Employee,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    val section: Section,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    val subject: Subject,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by", nullable = true)
    val assignedBy: User? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    val dayOfWeek: DayOfWeek,

    @Column(name = "time_start", nullable = false)
    val timeStart: LocalTime,

    @Column(name = "time_end", nullable = false)
    val timeEnd: LocalTime,

    @Column(name = "school_year", nullable = false, length = 20)
    val schoolYear: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ScheduleStatus = ScheduleStatus.ACTIVE,

    // var instead of val so @PrePersist can set it reliably
    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

) {
    /**
     * Returns the id after the entity has been persisted.
     * Throws [IllegalStateException] if called on an unpersisted entity.
     */
    fun requireId(): UUID = id
        ?: error("TeacherSchedule has not been persisted yet — id is null")

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