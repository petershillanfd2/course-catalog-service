package com.kotlinspring.coursecatalogservice.service

import com.kotlinspring.coursecatalogservice.entity.Instructor
import com.kotlinspring.coursecatalogservice.repository.InstructorRepository
import com.kotlinspring.coursecatalogservice.util.instructor
import com.kotlinspring.coursecatalogservice.util.instructorDTO
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class InstructorServiceTest {
    @MockK
    private lateinit var instructorRepository: InstructorRepository

    private lateinit var instructorService: InstructorService

    @BeforeEach
    fun setUp() {
        instructorService = InstructorService(instructorRepository)
    }

    @Test
    fun createInstructor() {
        every { instructorRepository.save(any<Instructor>()) } returns instructor()

        val instructorDTO = instructorDTO()
        val result = instructorService.createInstructor(instructorDTO)
        assertThat(result).isEqualTo(instructorDTO)

        verify(exactly = 1) {
            instructorRepository.save(any<Instructor>())
        }
    }
}