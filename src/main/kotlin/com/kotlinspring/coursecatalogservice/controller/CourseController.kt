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
    fun addCourse(@RequestBody @Valid courseDTO: CourseDTO): CourseDTO =
        courseService.addCourse(courseDTO)

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun retrieveAllCourses(
        @RequestParam(
            "course_name",
            required = false
        ) courseName: String?
    ): List<CourseDTO> = courseService.retrieveAllCourses(courseName)

    @PutMapping("/{course_id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCourse(
        @RequestBody @Valid courseDTO: CourseDTO,
        @PathVariable("course_id") courseId: Int
    ): CourseDTO = courseService.updateCourse(courseDTO, courseId)

    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable("course_id") courseId: Int) =
        courseService.deleteCourse(courseId)
}
