package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SubjectRepository : JpaRepository<Subject, UUID> {
    fun findBySchoolId(schoolId: UUID): List<Subject>
    fun findBySchoolIdAndStudentLevel(schoolId: UUID, studentLevel: Int): List<Subject>
    fun findBySubjectAreaId(subjectAreaId: UUID): List<Subject>
    fun findBySubjectCode(subjectCode: String): Subject?
    fun existsBySubjectCode(subjectCode: String): Boolean
    fun existsBySchoolIdAndSubjectName(schoolId: UUID, subjectName: String): Boolean
}