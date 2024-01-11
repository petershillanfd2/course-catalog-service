package com.kotlinspring.coursecatalogservice.controller

import com.kotlinspring.coursecatalogservice.service.GreetingService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

private const val NAME = "Peter"

@WebMvcTest(controllers = [GreetingController::class])
@ActiveProfiles("test")
class GreetingControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    lateinit var greetingServiceMock: GreetingService

    @Test
    fun retrieveGreeting() {
        every {
            greetingServiceMock.retrieveGreeting(any())
        } returns "$NAME, Hello from default profile"

        mockMvc.perform(get("/v1/greetings/{name}", NAME))
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(content().string("$NAME, Hello from default profile"))
    }
}