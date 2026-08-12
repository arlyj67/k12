package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.SubjectAreaRequest
import com.schoolproject.k12.dto.response.SubjectAreaResponse
import com.schoolproject.k12.mapper.SubjectAreaMapper
import com.schoolproject.k12.repository.SchoolRepository
import com.schoolproject.k12.repository.SubjectAreaRepository
import com.schoolproject.k12.service.SubjectAreaService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class SubjectAreaServiceImpl(
    private val subjectAreaRepository: SubjectAreaRepository,
    private val schoolRepository: SchoolRepository,
    private val subjectAreaMapper: SubjectAreaMapper
) : SubjectAreaService {

    override fun createSubjectArea(dto: SubjectAreaRequest): SubjectAreaResponse {
        val school = schoolRepository.findById(dto.schoolId)
            .orElseThrow { NoSuchElementException("School not found") }

        if (subjectAreaRepository.existsBySchoolIdAndAreaName(dto.schoolId, dto.areaName)) {
            throw IllegalArgumentException("Subject area already exists: ${dto.areaName}")
        }

        val subjectArea = subjectAreaRepository.save(subjectAreaMapper.toEntity(dto, school))
        return subjectAreaMapper.toResponse(subjectArea)
    }

    override fun getAllBySchool(schoolId: UUID): List<SubjectAreaResponse> =
        subjectAreaMapper.toResponseList(subjectAreaRepository.findBySchoolId(schoolId))

    override fun getActiveBySchool(schoolId: UUID): List<SubjectAreaResponse> =
        subjectAreaMapper.toResponseList(subjectAreaRepository.findBySchoolIdAndIsActive(schoolId, true))

    override fun getById(id: UUID): SubjectAreaResponse =
        subjectAreaMapper.toResponse(
            subjectAreaRepository.findById(id)
                .orElseThrow { NoSuchElementException("Subject area not found") }
        )

    override fun updateSubjectArea(id: UUID, dto: SubjectAreaRequest): SubjectAreaResponse {
        val subjectArea = subjectAreaRepository.findById(id)
            .orElseThrow { NoSuchElementException("Subject area not found") }

        subjectArea.areaName = dto.areaName
        subjectArea.description = dto.description
        subjectArea.isActive = dto.isActive
        subjectArea.updatedAt = LocalDateTime.now()

        return subjectAreaMapper.toResponse(subjectAreaRepository.save(subjectArea))
    }

    override fun deleteSubjectArea(id: UUID) {
        if (!subjectAreaRepository.existsById(id)) {
            throw NoSuchElementException("Subject area not found")
        }
        subjectAreaRepository.deleteById(id)
    }
}