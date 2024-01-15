package com.kotlinspring.coursecatalogservice.service

import com.kotlinspring.coursecatalogservice.dto.InstructorDTO
import com.kotlinspring.coursecatalogservice.entity.Course
import com.kotlinspring.coursecatalogservice.entity.Instructor
import com.kotlinspring.coursecatalogservice.exception.InstructorNotFoundException
import com.kotlinspring.coursecatalogservice.repository.InstructorRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class InstructorService(val instructorRepository: InstructorRepository) {
    private val logger = KotlinLogging.logger {}

    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO {
        val instructor = instructorDTO.let {
            Instructor(it.id, it.name)
        }

        val savedInstructor = instructorRepository.save(instructor)

        logger.info { "createInstructor() id: ${savedInstructor.id}" }

        return savedInstructor.let {
            InstructorDTO(it.id, it.name)
        }
    }

    fun findInstructorById(instructorId: Int): InstructorDTO {
        val instructor = findInstructor(instructorId)
        return InstructorDTO(instructor.id, instructor.name)
    }

    internal fun setCourseInstructor(course: Course, instructorId: Int): Course {
        val instructor = findInstructor(instructorId)
        return Course(course.id, course.name, course.category, instructor)
    }

    private fun findInstructor(instructorId: Int): Instructor {
        val instructorOptional = instructorRepository.findById(instructorId)
        if (instructorOptional.isPresent) {
            return instructorOptional.get()
        } else {
            throw InstructorNotFoundException("instructor not found for id: $instructorId")
        }
    }
}
