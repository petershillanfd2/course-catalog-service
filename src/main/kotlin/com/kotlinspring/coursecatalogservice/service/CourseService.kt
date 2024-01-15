package com.kotlinspring.coursecatalogservice.service

import com.kotlinspring.coursecatalogservice.dto.CourseDTO
import com.kotlinspring.coursecatalogservice.entity.Course
import com.kotlinspring.coursecatalogservice.exception.CourseNotFoundException
import com.kotlinspring.coursecatalogservice.exception.InstructorNotFoundException
import com.kotlinspring.coursecatalogservice.repository.CourseRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    val courseRepository: CourseRepository,
    val instructorService: InstructorService
) {
    private val logger = KotlinLogging.logger {}

    fun addCourse(courseDTO: CourseDTO): CourseDTO {
        val course = courseDTO.let {
            instructorService.setCourseInstructor(Course(null, it.name, it.category), courseDTO.instructorId!!)
        }

        val savedCourse = courseRepository.save(course)

        logger.info { "addCourse() id: ${savedCourse.id}" }

        return savedCourse.let {
            CourseDTO(it.id, it.name, it.category, it.instructor?.id)
        }
    }

    fun retrieveAllCourses(courseName: String? = null): List<CourseDTO> {
        val courses = courseName?.let {
            courseRepository.findByNameContaining(courseName)
        } ?: courseRepository.findAll()

        return courses.map {
            CourseDTO(it.id, it.name, it.category, it.instructor?.id)
        }
    }

    fun updateCourse(courseDTO: CourseDTO, courseId: Int): CourseDTO {
        val existingCourse = courseRepository.findById(courseId)
        return if (existingCourse.isPresent) {
            existingCourse.get().let {
                it.name = courseDTO.name
                it.category = courseDTO.category
                val savedCourse = courseRepository.save(it)

                CourseDTO(savedCourse.id, savedCourse.name, savedCourse.category, savedCourse.instructor?.id)
            }
        } else {
            throw CourseNotFoundException("id not found")
        }
    }

    fun deleteCourse(courseId: Int): Unit {
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId)
        } else {
            throw CourseNotFoundException("id not found")
        }
    }
}
