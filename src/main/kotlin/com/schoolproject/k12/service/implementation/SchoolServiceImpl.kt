package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.SchoolRequest
import com.schoolproject.k12.dto.response.SchoolResponse
import com.schoolproject.k12.mapper.SchoolMapper
import com.schoolproject.k12.repository.SchoolRepository
import com.schoolproject.k12.service.SchoolService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class SchoolServiceImpl(
    private val schoolRepository: SchoolRepository,
    private val schoolMapper: SchoolMapper,
) : SchoolService {

    override fun createSchool(dto: SchoolRequest): SchoolResponse {
        if (schoolRepository.existsBySchoolName(dto.schoolName)) {
            throw IllegalStateException("School already exists: ${dto.schoolName}")
        }
        val school = schoolRepository.save(schoolMapper.toEntity(dto))
        return schoolMapper.toResponse(school)
    }

    override fun getAllSchools(): List<SchoolResponse> =
        schoolRepository.findAll().map { schoolMapper.toResponse(it) }

    override fun getSchoolById(id: UUID): SchoolResponse =
        schoolRepository.findById(id)
            .map { schoolMapper.toResponse(it) }
            .orElseThrow { NoSuchElementException("School not found!") }

    override fun getActiveSchools(): List<SchoolResponse> =
        schoolRepository.findByIsActive(true)
            .map { schoolMapper.toResponse(it) }

    override fun updateSchool(id: UUID, dto: SchoolRequest): SchoolResponse {
        val school = schoolRepository.findById(id)
            .orElseThrow { NoSuchElementException("School not found") }
        school.schoolName = dto.schoolName
        school.address = dto.address
        school.contactNumber = dto.contactNumber
        school.email = dto.email
        school.principalName = dto.principalName
        school.isActive = dto.isActive
        return schoolMapper.toResponse(schoolRepository.save(school))
    }

    override fun deleteSchool(id: UUID) {
        if (!schoolRepository.existsById(id)) {
            throw NoSuchElementException("School not found")
        }
        schoolRepository.deleteById(id)
    }
}