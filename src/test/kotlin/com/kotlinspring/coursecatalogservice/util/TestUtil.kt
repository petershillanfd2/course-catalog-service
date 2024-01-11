package com.kotlinspring.coursecatalogservice.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kotlinspring.coursecatalogservice.dto.CourseDTO
import com.kotlinspring.coursecatalogservice.entity.Course

fun course() = Course(1, "Starting Marketing 1", "Marketing")

fun courseList() = listOf(
    course(),
    Course(2, "Starting Business 1", "Business"),
    Course(3, "Starting Development 1", "Development"),
    Course(4, "Starting SpringBoot 1", "Development"),
    Course(5, "Mastering SpringBoot 1", "Development")
)

fun courseDTO() = CourseDTO(1, "Starting Marketing 1", "Marketing")

fun courseDTOList() = listOf(
    courseDTO(),
    CourseDTO(2, "Starting Business 1", "Business"),
    CourseDTO(3, "Starting Development 1", "Development"),
    CourseDTO(4, "Starting SpringBoot 1", "Development"),
    CourseDTO(5, "Mastering SpringBoot 1", "Development")
)

fun courseToDTO(course: Course) = CourseDTO(course.id, course.name, course.category)
fun courseDTOToCourse(courseDTO: CourseDTO) =
    Course(courseDTO.id, courseDTO.name, courseDTO.category)

inline fun <reified T> asJsonString(obj: T): String = jacksonObjectMapper().writeValueAsString(obj)

inline fun <reified T> asObject(str: String): T =
    jacksonObjectMapper().readValue(str, T::class.java)

inline fun <reified T> asList(str: String): List<T> =
    jacksonObjectMapper().readValue(str)
