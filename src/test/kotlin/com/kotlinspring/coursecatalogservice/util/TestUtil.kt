package com.kotlinspring.coursecatalogservice.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kotlinspring.coursecatalogservice.dto.CourseDTO
import com.kotlinspring.coursecatalogservice.dto.InstructorDTO
import com.kotlinspring.coursecatalogservice.entity.Course
import com.kotlinspring.coursecatalogservice.entity.Instructor

fun course(instructorId: Int = 1) =
    Course(1, "Starting Marketing 1", "Marketing", instructor(instructorId))

fun courseList(instructorId: Int = 1): List<Course> {
    val instructor = instructor(instructorId)

    return listOf(
        course(instructorId),
        Course(2, "Starting Business 1", "Business", instructor),
        Course(3, "Starting Development 1", "Development", instructor),
        Course(4, "Starting SpringBoot 1", "Development", instructor),
        Course(5, "Mastering SpringBoot 1", "Development", instructor)
    )
}

fun courseDTO(instructorId: Int = 1) =
    CourseDTO(instructorId, "Starting Marketing 1", "Marketing", instructorId)

fun courseDTOList(instructorId: Int = 1) = listOf(
    courseDTO(),
    CourseDTO(2, "Starting Business 1", "Business", instructorId),
    CourseDTO(3, "Starting Development 1", "Development", instructorId),
    CourseDTO(4, "Starting SpringBoot 1", "Development", instructorId),
    CourseDTO(5, "Mastering SpringBoot 1", "Development", instructorId)
)

fun courseToDTO(course: Course) = CourseDTO(course.id, course.name, course.category, course.instructor?.id)

fun instructor(instructorId: Int = 1) = Instructor(instructorId, "Peter Shillan")

fun instructorDTO(instructorId: Int = 1) = InstructorDTO(instructorId, "Peter Shillan")

inline fun <reified T> asJsonString(obj: T): String = jacksonObjectMapper().writeValueAsString(obj)

inline fun <reified T> asObject(str: String): T =
    jacksonObjectMapper().readValue(str, T::class.java)

inline fun <reified T> asList(str: String): List<T> =
    jacksonObjectMapper().readValue(str)
