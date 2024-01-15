package com.kotlinspring.coursecatalogservice.controller

import com.kotlinspring.coursecatalogservice.dto.InstructorDTO
import com.kotlinspring.coursecatalogservice.service.InstructorService
import com.kotlinspring.coursecatalogservice.util.asJsonString
import com.kotlinspring.coursecatalogservice.util.asObject
import com.kotlinspring.coursecatalogservice.util.instructorDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(InstructorController::class)
@ActiveProfiles("test")
class InstructorControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    private lateinit var instructorServiceMock: InstructorService

    @Test
    fun createInstructor() {
        val instructorDTO = instructorDTO()
        val instructorJsonStr = asJsonString(instructorDTO)

        every {
            instructorServiceMock.createInstructor(any())
        } returns instructorDTO

        val responseString = mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(instructorJsonStr)
        ).andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(
                MockMvcResultMatchers.content()
                    .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.content().json(instructorJsonStr))
            .andReturn().response.contentAsString

        val result = asObject<InstructorDTO>(responseString)
        Assertions.assertThat(result).isEqualTo(instructorDTO)
    }

}