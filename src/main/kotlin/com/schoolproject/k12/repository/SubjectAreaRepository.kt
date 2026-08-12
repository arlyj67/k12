package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.SubjectArea
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SubjectAreaRepository : JpaRepository<SubjectArea, UUID> {
    fun findBySchoolId(schoolId: UUID): List<SubjectArea>
    fun findBySchoolIdAndIsActive(schoolId: UUID, isActive: Boolean): List<SubjectArea>
    fun existsBySchoolIdAndAreaName(schoolId: UUID, areaName: String): Boolean
}