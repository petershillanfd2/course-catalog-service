package com.kotlinspring.coursecatalogservice.controller

import com.kotlinspring.coursecatalogservice.dto.CourseDTO
import com.kotlinspring.coursecatalogservice.exception.CourseNotFoundException
import com.kotlinspring.coursecatalogservice.service.CourseService
import com.kotlinspring.coursecatalogservice.util.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

private const val COURSE_NAME_EXPRESSION = "SpringBoot"

@WebMvcTest(CourseController::class)
@ActiveProfiles("test")
class CourseControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    private lateinit var courseServiceMock: CourseService

    @Test
    fun addCourse() {
        val courseDTO = courseDTO()
        val courseJsonStr = asJsonString(courseDTO)

        every {
            courseServiceMock.addCourse(any())
        } returns courseDTO

        val responseString = mockMvc.perform(
            post("/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courseJsonStr)
        ).andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(courseJsonStr))
            .andReturn().response.contentAsString

        val result = asObject<CourseDTO>(responseString)
        assertThat(result).isEqualTo(courseDTO)
    }

    @Test
    fun addCourse_validateName() {
        val courseDTO = CourseDTO(null, "", "")
        val courseJsonStr = asJsonString(courseDTO)

        every {
            courseServiceMock.addCourse(any())
        } returns courseDTO()

        mockMvc.perform(
            post("/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courseJsonStr)
        ).andExpect(status().isBadRequest)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(
                jsonPath("$.errors", hasItems("CourseDTO.name must not be blank"))
            )
            .andExpect(
                jsonPath("$.errors", hasItems("CourseDTO.category must not be blank"))
            )
    }

    @Test
    fun retrieveCourses() {
        val courses = courseDTOList()
        every {
            courseServiceMock.retrieveAllCourses()
        } returns courses

        val responseString = mockMvc.perform(
            get("/v1/courses")
        ).andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize<CourseDTO>(5)))
            .andReturn().response.contentAsString

        val results: List<CourseDTO> = asList(responseString)
        assertThat(results).isEqualTo(courses)
    }

    @Test
    fun retrieveAllCoursesByName() {
        val courses = courseDTOList().filter { c ->
            c.name.contains(
                COURSE_NAME_EXPRESSION
            )
        }

        every {
            courseServiceMock.retrieveAllCourses(COURSE_NAME_EXPRESSION)
        } returns courses

        val responseString = mockMvc.perform(
            get("/v1/courses")
                .param("course_name", COURSE_NAME_EXPRESSION)
        ).andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize<CourseDTO>(2)))
            .andReturn().response.contentAsString

        val results: List<CourseDTO> = asList(responseString)
        assertThat(results).isEqualTo(courses)
    }

    @Test
    fun updateCourse() {
        val course = courseDTO()

        every {
            courseServiceMock.updateCourse(any(), any())
        } returns course

        val responseString = mockMvc.perform(
            put("/v1/courses/{course_id}", course.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(course))
        ).andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().response.contentAsString

        val result = asObject<CourseDTO>(responseString)
        assertThat(result).isEqualTo(course)
    }

    @Test
    fun updateCourseNotFound() {
        val course = courseDTO()

        every {
            courseServiceMock.updateCourse(any(), any())
        } throws CourseNotFoundException("id not found")

        mockMvc.perform(
            put("/v1/courses/{course_id}", course.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(course))
        ).andExpect(status().isNotFound)
            .andExpect(content().string("id not found"))
    }

    @Test
    fun deleteCourse() {
        val course = courseDTO()

        every { courseServiceMock.deleteCourse(course.id!!) } just runs

        mockMvc.perform(
            delete("/v1/courses/{course_id}", course.id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)
    }

    @Test
    fun deleteCourseNotFound() {
        val course = courseDTO()

        every { courseServiceMock.deleteCourse(course.id!!) } throws CourseNotFoundException("id not found")

        mockMvc.perform(
            delete("/v1/courses/{course_id}", course.id)
        ).andExpect(status().isNotFound)
            .andExpect(content().string("id not found"))
    }
}