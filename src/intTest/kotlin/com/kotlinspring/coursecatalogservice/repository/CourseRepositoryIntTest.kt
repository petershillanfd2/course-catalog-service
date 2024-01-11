package com.kotlinspring.coursecatalogservice.repository

import com.kotlinspring.coursecatalogservice.entity.Course
import com.kotlinspring.coursecatalogservice.util.courseList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntTest(@Autowired val courseRepository: CourseRepository) {
    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        courseRepository.saveAll(courseList())
    }

    @Test
    fun findByNameContaining() {
        val results = courseRepository.findByNameContaining("SpringBoot")

        assertThat(results)
            .map<String>(Course::name)
            .contains("Starting SpringBoot 1", "Mastering SpringBoot 1")
    }
}
