package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.TeacherSchedule
import com.schoolproject.k12.model.DayOfWeek
import com.schoolproject.k12.model.ScheduleStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalTime
import java.util.UUID

@Repository
interface TeacherScheduleRepository : JpaRepository<TeacherSchedule, UUID> {

    fun findByEmployeeId(employeeId: UUID): List<TeacherSchedule>
    fun findBySectionId(sectionId: UUID): List<TeacherSchedule>
    fun findBySubjectId(subjectId: UUID): List<TeacherSchedule>
    fun findBySchoolYear(schoolYear: String): List<TeacherSchedule>
    fun findByEmployeeIdAndSchoolYear(employeeId: UUID, schoolYear: String): List<TeacherSchedule>
    fun findBySectionIdAndSchoolYear(sectionId: UUID, schoolYear: String): List<TeacherSchedule>
    fun findByStatus(status: ScheduleStatus): List<TeacherSchedule>

    // Added for getSchedulesBySchool — navigates through employee.school
    fun findByEmployeeSchoolId(schoolId: UUID): List<TeacherSchedule>

    // Check for conflicting schedule — same teacher, same day, overlapping time
    @Query("""
        SELECT COUNT(ts) > 0 FROM TeacherSchedule ts 
        WHERE ts.employee.id = :employeeId 
        AND ts.dayOfWeek = :dayOfWeek 
        AND ts.status = 'ACTIVE'
        AND (ts.timeStart < :timeEnd AND ts.timeEnd > :timeStart)
    """)
    fun hasConflict(
        employeeId: UUID,
        dayOfWeek: DayOfWeek,
        timeStart: LocalTime,
        timeEnd: LocalTime
    ): Boolean

    // Check for conflicting schedule — same section, same day, overlapping time
    @Query("""
        SELECT COUNT(ts) > 0 FROM TeacherSchedule ts 
        WHERE ts.section.id = :sectionId 
        AND ts.dayOfWeek = :dayOfWeek 
        AND ts.status = 'ACTIVE'
        AND (ts.timeStart < :timeEnd AND ts.timeEnd > :timeStart)
    """)
    fun hasSectionConflict(
        sectionId: UUID,
        dayOfWeek: DayOfWeek,
        timeStart: LocalTime,
        timeEnd: LocalTime
    ): Boolean

    // Same as hasConflict but excludes the schedule being updated — prevents false conflict on self
    @Query("""
        SELECT COUNT(ts) > 0 FROM TeacherSchedule ts 
        WHERE ts.employee.id = :employeeId 
        AND ts.dayOfWeek = :dayOfWeek 
        AND ts.status = 'ACTIVE'
        AND ts.id <> :excludeId
        AND (ts.timeStart < :timeEnd AND ts.timeEnd > :timeStart)
    """)
    fun hasConflictExcluding(
        employeeId: UUID,
        dayOfWeek: DayOfWeek,
        timeStart: LocalTime,
        timeEnd: LocalTime,
        excludeId: UUID
    ): Boolean

    // Same as hasSectionConflict but excludes the schedule being updated
    @Query("""
        SELECT COUNT(ts) > 0 FROM TeacherSchedule ts 
        WHERE ts.section.id = :sectionId 
        AND ts.dayOfWeek = :dayOfWeek 
        AND ts.status = 'ACTIVE'
        AND ts.id <> :excludeId
        AND (ts.timeStart < :timeEnd AND ts.timeEnd > :timeStart)
    """)
    fun hasSectionConflictExcluding(
        sectionId: UUID,
        dayOfWeek: DayOfWeek,
        timeStart: LocalTime,
        timeEnd: LocalTime,
        excludeId: UUID
    ): Boolean
}