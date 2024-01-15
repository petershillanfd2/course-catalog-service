package com.kotlinspring.coursecatalogservice.service

import com.kotlinspring.coursecatalogservice.dto.InstructorDTO
import com.kotlinspring.coursecatalogservice.entity.Instructor
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

    fun findInstructorById(instructorId: Int) = instructorRepository.findById(instructorId)
}
