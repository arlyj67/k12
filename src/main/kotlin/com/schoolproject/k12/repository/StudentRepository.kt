package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.Student
import com.schoolproject.k12.model.StudentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StudentRepository : JpaRepository<Student, UUID> {

    @Query("SELECT s.studentNumber FROM Student s WHERE s.school.id = :schoolId AND s.studentNumber LIKE :prefix% ORDER BY s.studentNumber DESC")
    fun findLatestStudentNumber(schoolId: UUID, prefix: String): String?

    fun findByStudentNumber(studentNumber: String): Student?

    fun findBySchoolId(schoolId: UUID): List<Student>

    fun findBySchoolIdAndStatus(schoolId: UUID, status: StudentStatus): List<Student>

    fun findByStatus(status: StudentStatus): List<Student>

    fun existsByStudentNumber(studentNumber: String): Boolean

    fun existsByUserId(userId: UUID): Boolean

    fun findByUserId(userId: UUID): Student?
}