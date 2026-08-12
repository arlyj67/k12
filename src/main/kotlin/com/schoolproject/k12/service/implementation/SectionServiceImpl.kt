package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.SectionRequest
import com.schoolproject.k12.dto.response.SectionResponse
import com.schoolproject.k12.mapper.SectionMapper
import com.schoolproject.k12.repository.EmployeeRepository
import com.schoolproject.k12.repository.RoomRepository
import com.schoolproject.k12.repository.SectionRepository
import com.schoolproject.k12.repository.SchoolRepository
import com.schoolproject.k12.service.SectionService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class SectionServiceImpl(
    private val sectionRepository: SectionRepository,
    private val schoolRepository: SchoolRepository,
    private val roomRepository: RoomRepository,
    private val employeeRepository: EmployeeRepository,
    private val sectionMapper: SectionMapper
) : SectionService {

    override fun createSection(dto: SectionRequest): SectionResponse {
        val school = schoolRepository.findById(dto.schoolId)
            .orElseThrow { NoSuchElementException("School not found") }

        val room = roomRepository.findById(dto.roomId)
            .orElseThrow { NoSuchElementException("Room not found") }

        val adviser = employeeRepository.findById(dto.adviserId)
            .orElseThrow { NoSuchElementException("Adviser not found") }


        if (sectionRepository.existsBySchoolIdAndSectionNameAndSchoolYear(
                dto.schoolId, dto.sectionName, dto.schoolYear)) {
            throw IllegalArgumentException("Section already exists: ${dto.sectionName} for ${dto.schoolYear}")
        }

        val section = sectionRepository.save(sectionMapper.toEntity(dto, room, adviser, school))
        return sectionMapper.toResponse(section)
    }

    override fun getAllBySchool(schoolId: UUID): List<SectionResponse> =
        sectionMapper.toResponseList(sectionRepository.findBySchoolId(schoolId))

    override fun getBySchoolAndSchoolYear(schoolId: UUID, schoolYear: String): List<SectionResponse> =
        sectionMapper.toResponseList(sectionRepository.findBySchoolIdAndSchoolYear(schoolId, schoolYear))

    override fun getBySchoolAndStudentLevel(schoolId: UUID, studentLevel: Int): List<SectionResponse> =
        sectionMapper.toResponseList(sectionRepository.findBySchoolIdAndStudentLevel(schoolId, studentLevel))

    override fun getByAdviser(adviserId: UUID): List<SectionResponse> =
        sectionMapper.toResponseList(sectionRepository.findByAdviserId(adviserId))

    override fun getById(id: UUID): SectionResponse =
        sectionMapper.toResponse(
            sectionRepository.findById(id)
                .orElseThrow { NoSuchElementException("Section not found") }
        )

    override fun updateSection(id: UUID, dto: SectionRequest): SectionResponse {
        val section = sectionRepository.findById(id)
            .orElseThrow { NoSuchElementException("Section not found") }

        val room = roomRepository.findById(dto.roomId)
            .orElseThrow { NoSuchElementException("Room not found") }

        val adviser = employeeRepository.findById(dto.adviserId)
            .orElseThrow { NoSuchElementException("Adviser not found") }

        section.sectionName = dto.sectionName
        section.studentLevel = dto.studentLevel
        section.schoolYear = dto.schoolYear
        section.room = room
        section.adviserId = adviser
        section.updatedAt = LocalDateTime.now()

        return sectionMapper.toResponse(sectionRepository.save(section))
    }

    override fun deleteSection(id: UUID) {
        if (!sectionRepository.existsById(id)) {
            throw NoSuchElementException("Section not found")
        }
        sectionRepository.deleteById(id)
    }
}