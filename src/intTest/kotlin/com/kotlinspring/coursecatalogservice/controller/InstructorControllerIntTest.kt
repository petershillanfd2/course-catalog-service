package com.kotlinspring.coursecatalogservice.controller

import com.kotlinspring.coursecatalogservice.dto.InstructorDTO
import com.kotlinspring.coursecatalogservice.repository.InstructorRepository
import com.kotlinspring.coursecatalogservice.util.Result
import com.kotlinspring.coursecatalogservice.util.asObject
import com.kotlinspring.coursecatalogservice.util.instructorDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class InstructorControllerIntTest(
    @Autowired val webTestClient: WebTestClient,
    @Autowired val instructorRepository: InstructorRepository
) {
    @Test
    fun createInstructor() {
        val instructor = instructorDTO()

        val result = webTestClient.post()
            .uri("/v1/instructors")
            .bodyValue(instructor)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody

        assertThat(result!!).isNotNull
        assertThat(result.id ?: 0).isGreaterThan(0)
        assertThat(result.name).isEqualTo(instructor.name)
    }

    @Test
    fun createInstructor_validateFields() {
        val instructor = InstructorDTO(null, "")

        val responseBody = webTestClient.post()
            .uri("/v1/instructors")
            .bodyValue(instructor)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        val results = asObject<Result>(responseBody!!)
        assertThat(results.errors)
            .hasSize(1)
            .containsOnly("InstructorDTO.name must not be blank")
    }
}