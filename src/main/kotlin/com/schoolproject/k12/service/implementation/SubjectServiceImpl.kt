package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.SubjectRequest
import com.schoolproject.k12.dto.response.SubjectResponse
import com.schoolproject.k12.mapper.SubjectMapper
import com.schoolproject.k12.repository.SchoolRepository
import com.schoolproject.k12.repository.SubjectAreaRepository
import com.schoolproject.k12.repository.SubjectRepository
import com.schoolproject.k12.service.SubjectService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class SubjectServiceImpl(
    private val subjectRepository: SubjectRepository,
    private val schoolRepository: SchoolRepository,
    private val subjectAreaRepository: SubjectAreaRepository,
    private val subjectMapper: SubjectMapper
) : SubjectService {

    override fun createSubject(dto: SubjectRequest): SubjectResponse {
        val school = schoolRepository.findById(dto.schoolId)
            .orElseThrow { NoSuchElementException("School not found") }

        val subjectArea = subjectAreaRepository.findById(dto.subjectAreaId)
            .orElseThrow { NoSuchElementException("Subject area not found") }

        if (subjectRepository.existsBySubjectCode(dto.subjectCode)) {
            throw IllegalArgumentException("Subject code already exists: ${dto.subjectCode}")
        }

        if (subjectRepository.existsBySchoolIdAndSubjectName(dto.schoolId, dto.subjectName)) {
            throw IllegalArgumentException("Subject already exists: ${dto.subjectName}")
        }

        val subject = subjectRepository.save(subjectMapper.toEntity(dto, school, subjectArea))
        return subjectMapper.toResponse(subject)
    }

    override fun getAllBySchool(schoolId: UUID): List<SubjectResponse> =
        subjectMapper.toResponseList(subjectRepository.findBySchoolId(schoolId))

    override fun getBySchoolAndStudentLevel(schoolId: UUID, studentLevel: Int): List<SubjectResponse> =
        subjectMapper.toResponseList(subjectRepository.findBySchoolIdAndStudentLevel(schoolId, studentLevel))

    override fun getBySubjectArea(subjectAreaId: UUID): List<SubjectResponse> =
        subjectMapper.toResponseList(subjectRepository.findBySubjectAreaId(subjectAreaId))

    override fun getBySubjectCode(subjectCode: String): SubjectResponse =
        subjectMapper.toResponse(
            subjectRepository.findBySubjectCode(subjectCode)
                ?: throw NoSuchElementException("Subject not found: $subjectCode")
        )

    override fun getById(id: UUID): SubjectResponse =
        subjectMapper.toResponse(
            subjectRepository.findById(id)
                .orElseThrow { NoSuchElementException("Subject not found") }
        )

    override fun updateSubject(id: UUID, dto: SubjectRequest): SubjectResponse {
        val subject = subjectRepository.findById(id)
            .orElseThrow { NoSuchElementException("Subject not found") }

        val subjectArea = subjectAreaRepository.findById(dto.subjectAreaId)
            .orElseThrow { NoSuchElementException("Subject area not found") }

        subject.subjectName = dto.subjectName
        subject.subjectCode = dto.subjectCode
        subject.description = dto.description
        subject.studentLevel = dto.studentLevel
        subject.subjectArea = subjectArea
        subject.updatedAt = LocalDateTime.now()

        return subjectMapper.toResponse(subjectRepository.save(subject))
    }

    override fun deleteSubject(id: UUID) {
        if (!subjectRepository.existsById(id)) {
            throw NoSuchElementException("Subject not found")
        }
        subjectRepository.deleteById(id)
    }
}