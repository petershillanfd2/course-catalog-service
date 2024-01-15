package com.kotlinspring.coursecatalogservice.controller

import com.kotlinspring.coursecatalogservice.dto.CourseDTO
import com.kotlinspring.coursecatalogservice.repository.CourseRepository
import com.kotlinspring.coursecatalogservice.repository.InstructorRepository
import com.kotlinspring.coursecatalogservice.util.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CourseControllerIntTest(
    @Autowired val webTestClient: WebTestClient,
    @Autowired val courseRepository: CourseRepository,
    @Autowired val instructorRepository: InstructorRepository
) {
    private var savedInstructorId = 0

    @BeforeEach
    fun setUp() {
        instructorRepository.deleteAll()
        savedInstructorId = instructorRepository.save(instructor()).id!!

        courseRepository.deleteAll()
        courseRepository.saveAll(courseList(savedInstructorId))
    }

    @Test
    fun addCourse() {
        val course = courseDTO(savedInstructorId)

        val result = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(course)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertThat(result!!).isNotNull
        assertThat(result.id ?: 0).isGreaterThan(0)
        assertThat(result.name).isEqualTo(course.name)
        assertThat(result.category).isEqualTo(course.category)
    }

    @Test
    fun addCourse_validateFields() {
        val course = CourseDTO(null, "", "")

        val responseBody = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(course)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        val results = asObject<Result>(responseBody!!)
        assertThat(results.errors)
            .hasSize(3)
            .containsOnly(
                "CourseDTO.name must not be blank",
                "CourseDTO.category must not be blank",
                "CourseDTO.instructorId must not be null"
            )
    }

    @Test
    fun retrieveAllCourses() {
        val results = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertThat(results!!)
            .isNotNull
            .hasSize(5)
            .map(CourseDTO::name, CourseDTO::category)
            .containsOnly(
                tuple("Starting Marketing 1", "Marketing"),
                tuple("Starting Business 1", "Business"),
                tuple("Starting Development 1", "Development"),
                tuple("Starting SpringBoot 1", "Development"),
                tuple("Mastering SpringBoot 1", "Development")
            )
    }

    @Test
    fun retrieveAllCoursesByName() {
        val results = webTestClient.get()
            .uri(
                UriComponentsBuilder.fromUriString("/v1/courses")
                    .queryParam("course_name", "SpringBoot").toUriString()
            )
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertThat(results!!)
            .isNotNull
            .hasSize(2)
            .map(CourseDTO::name, CourseDTO::category)
            .containsOnly(
                tuple("Starting SpringBoot 1", "Development"),
                tuple("Mastering SpringBoot 1", "Development")
            )
    }

    @Test
    fun updateCourse() {
        val course = courseRepository.findAll().first()
        val updateCourseId = course.id
        val instructorId = course.instructor!!.id
        val updateCourseDTO = CourseDTO(updateCourseId, "New Course", "Business", instructorId)

        val result = webTestClient.put()
            .uri("/v1/courses/{course_id}", updateCourseId)
            .bodyValue(updateCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertThat(result!!)
            .isNotNull
            .isEqualTo(updateCourseDTO)
    }

    @Test
    fun updateCourseNotFound() {
        val updateCourseId = -1
        val updateCourseDTO = CourseDTO(updateCourseId, "New Course", "Business", 1)

        val result = webTestClient.put()
            .uri("/v1/courses/{course_id}", updateCourseId)
            .bodyValue(updateCourseDTO)
            .exchange()
            .expectStatus().isNotFound
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertThat(result!!)
            .isNotNull()
            .isEqualTo("id not found")
    }

    @Test
    fun deleteCourse() {
        val updateCourseId = courseRepository.findAll().first().id

        webTestClient.delete()
            .uri("/v1/courses/{course_id}", updateCourseId)
            .exchange()
            .expectStatus().isNoContent
    }


    @Test
    fun deleteCourseNotFound() {
        val updateCourseId = -1

        val result = webTestClient.delete()
            .uri("/v1/courses/{course_id}", updateCourseId)
            .exchange()
            .expectStatus().isNotFound
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertThat(result!!)
            .isNotNull()
            .isEqualTo("id not found")
    }
}