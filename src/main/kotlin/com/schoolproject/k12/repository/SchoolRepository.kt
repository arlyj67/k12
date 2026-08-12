package com.schoolproject.k12.repository

import com.schoolproject.k12.entity.School
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SchoolRepository : JpaRepository<School, UUID> {

    fun findBySchoolName(schoolName: String): School?

    fun findByIsActive(isActive: Boolean): List<School>

    fun existsBySchoolName(schoolName: String): Boolean
}