package com.kotlinspring.coursecatalogservice.controller

import com.kotlinspring.coursecatalogservice.dto.CourseDTO
import com.kotlinspring.coursecatalogservice.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/courses")
class CourseController(val courseService: CourseService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@Valid @RequestBody courseDTO: CourseDTO): CourseDTO =
        courseService.addCourse(courseDTO)

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun retrieveAllCourses(): List<CourseDTO> = courseService.retrieveAllCourses()

    @PutMapping("/{course_id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCourse(
        @Valid @RequestBody courseDTO: CourseDTO,
        @PathVariable("course_id") courseId: Int
    ): CourseDTO = courseService.updateCourse(courseDTO, courseId)

    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable("course_id") courseId: Int) =
        courseService.deleteCourse(courseId)
}
