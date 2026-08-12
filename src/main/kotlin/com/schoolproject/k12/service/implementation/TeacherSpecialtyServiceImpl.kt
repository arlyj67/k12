package com.schoolproject.k12.service.implementation

import com.schoolproject.k12.dto.request.TeacherSpecialtyRequest
import com.schoolproject.k12.dto.response.TeacherSpecialtyResponse
import com.schoolproject.k12.entity.TeacherSpecialty
import com.schoolproject.k12.mapper.TeacherSpecialtyMapper
import com.schoolproject.k12.repository.EmployeeRepository
import com.schoolproject.k12.repository.SubjectAreaRepository
import com.schoolproject.k12.repository.TeacherSpecialtyRepository
import com.schoolproject.k12.service.TeacherSpecialtyService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TeacherSpecialtyServiceImpl(
    private val teacherSpecialtyRepository: TeacherSpecialtyRepository,
    private val employeeRepository: EmployeeRepository,
    private val subjectAreaRepository: SubjectAreaRepository,
    private val teacherSpecialtyMapper: TeacherSpecialtyMapper  // injected
) : TeacherSpecialtyService {

    override fun addSpecialty(request: TeacherSpecialtyRequest): List<TeacherSpecialtyResponse> {
        val employee = employeeRepository.findById(request.employeeId)
            .orElseThrow { NoSuchElementException("Employee not found") }

        if (employee.role.name != "TEACHER") {
            throw IllegalArgumentException("Employee is not a teacher")
        }

        val specialties = request.subjectAreaIds.map { subjectAreaId ->
            val subjectArea = subjectAreaRepository.findById(subjectAreaId)
                .orElseThrow { NoSuchElementException("Subject area not found: $subjectAreaId") }

            if (teacherSpecialtyRepository.existsByEmployeeIdAndSubjectAreaId(request.employeeId, subjectAreaId)) {
                throw IllegalArgumentException("Teacher already has specialty: ${subjectArea.areaName}")
            }

            teacherSpecialtyRepository.save(
                TeacherSpecialty(
                    employee = employee,
                    subjectArea = subjectArea
                )
            )
        }

        return teacherSpecialtyMapper.toResponseList(specialties)
    }

    override fun getSpecialtiesByTeacher(employeeId: UUID): List<TeacherSpecialtyResponse> =
        teacherSpecialtyMapper.toResponseList(
            teacherSpecialtyRepository.findByEmployeeId(employeeId)
        )


    override fun getTeachersBySubjectArea(subjectAreaId: UUID): List<TeacherSpecialtyResponse> =
        teacherSpecialtyMapper.toResponseList(
            teacherSpecialtyRepository.findBySubjectAreaId(subjectAreaId)
        )

    override fun removeSpecialty(employeeId: UUID, subjectAreaId: UUID) {
        if (!teacherSpecialtyRepository.existsByEmployeeIdAndSubjectAreaId(employeeId, subjectAreaId)) {
            throw NoSuchElementException("Specialty not found")
        }
        teacherSpecialtyRepository.deleteByEmployeeIdAndSubjectAreaId(employeeId, subjectAreaId)
    }
}