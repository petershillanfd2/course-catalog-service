package com.kotlinspring.coursecatalogservice.controller

import com.kotlinspring.coursecatalogservice.dto.InstructorDTO
import com.kotlinspring.coursecatalogservice.service.InstructorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/instructors")
class InstructorController(val instructorService: InstructorService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInstructor(@RequestBody @Valid instructorDTO: InstructorDTO): InstructorDTO =
        instructorService.createInstructor(instructorDTO)

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getInstructor(@RequestParam("instructor_id") instructorId: Int) =
        instructorService.findInstructorById(instructorId)
}
