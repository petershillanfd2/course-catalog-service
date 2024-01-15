package com.kotlinspring.coursecatalogservice.repository

import com.kotlinspring.coursecatalogservice.entity.Course
import com.kotlinspring.coursecatalogservice.util.courseList
import com.kotlinspring.coursecatalogservice.util.instructor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntTest(
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
    fun findByNameContaining() {
        val results = courseRepository.findByNameContaining("SpringBoot")

        assertThat(results)
            .map<String>(Course::name)
            .contains("Starting SpringBoot 1", "Mastering SpringBoot 1")
    }

    @Test
    fun findByName() {
        val results = courseRepository.findByName("SpringBoot")

        assertThat(results)
            .map<String>(Course::name)
            .contains("Starting SpringBoot 1", "Mastering SpringBoot 1")
    }

    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun findByName_approach2(name: String, expectedSize: Int) {
        val results = courseRepository.findByName(name)

        assertThat(results)
            .hasSize(expectedSize)
    }

    companion object {
        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("SpringBoot", 2),
                Arguments.arguments("Starting", 4)
            )
        }
    }
}
