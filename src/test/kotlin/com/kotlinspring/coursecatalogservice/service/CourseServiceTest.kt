package com.kotlinspring.coursecatalogservice.service

import com.kotlinspring.coursecatalogservice.entity.Course
import com.kotlinspring.coursecatalogservice.repository.CourseRepository
import com.kotlinspring.coursecatalogservice.util.*
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

private const val COURSE_NAME_EXPRESSION = "SpringBoot"

@ExtendWith(MockKExtension::class)
class CourseServiceTest {
    @MockK
    private lateinit var courseRepositoryMock: CourseRepository

    @MockK
    private lateinit var instructorServiceMock: InstructorService

    private lateinit var courseService: CourseService

    @BeforeEach
    fun setUp() {
        courseService = CourseService(courseRepositoryMock, instructorServiceMock)
    }

    @Test
    fun addCourse() {
        every { courseRepositoryMock.save(any<Course>()) } returns course()
        every { instructorServiceMock.findInstructorById(any<Int>()) } returns Optional.of(
            instructor()
        )

        val result = courseService.addCourse(courseDTO())
        assertThat(result)
            .isEqualTo(courseDTO())

        verify(exactly = 1) {
            courseRepositoryMock.save(any<Course>())
            instructorServiceMock.findInstructorById(any<Int>())
        }
    }

    @Test
    fun retrieveAllCourses() {
        every { courseRepositoryMock.findAll() } returns courseList()

        val results = courseService.retrieveAllCourses()

        assertThat(results)
            .isEqualTo(courseDTOList())

        verify(exactly = 1) {
            courseRepositoryMock.findAll()
        }
    }

    @Test
    fun retrieveAllCoursesByName() {
        every { courseRepositoryMock.findByNameContaining(COURSE_NAME_EXPRESSION) } returns
                courseList().filter { c ->
                    c.name.contains(
                        COURSE_NAME_EXPRESSION
                    )
                }

        val results = courseService.retrieveAllCourses(COURSE_NAME_EXPRESSION)

        assertThat(results)
            .isEqualTo(courseDTOList().filter { c ->
                c.name.contains(
                    COURSE_NAME_EXPRESSION
                )
            })

        verify(exactly = 1) {
            courseRepositoryMock.findByNameContaining(COURSE_NAME_EXPRESSION)
        }
    }

    @Test
    fun updateCourse() {
        val savedCourse = course()
        val savedCourseId: Int = savedCourse.id!!

        every { courseRepositoryMock.findById(eq(savedCourseId)) } returns Optional.of(savedCourse)
        every { courseRepositoryMock.save(any<Course>()) } returns savedCourse

        val result = courseService.updateCourse(courseDTO(), savedCourseId)

        assertThat(result)
            .isEqualTo(courseToDTO(savedCourse))

        verify(exactly = 1) {
            courseRepositoryMock.findById(eq(savedCourseId))
            courseRepositoryMock.save(any<Course>())
        }
    }

    @Test
    fun updateCourseNotFound() {
        val savedCourse = course()
        val savedCourseId: Int = savedCourse.id!!

        every { courseRepositoryMock.findById(eq(savedCourseId)) } returns Optional.empty()

        assertThatThrownBy {
            courseService.updateCourse(
                courseDTO(),
                savedCourseId
            )
        }.hasMessage("id not found")

        verify(exactly = 1) {
            courseRepositoryMock.findById(eq(savedCourseId))
        }
    }

    @Test
    fun deleteCourse() {
        val savedCourse = course()
        val savedCourseId: Int = savedCourse.id!!

        every { courseRepositoryMock.existsById(savedCourseId) } returns true
        every { courseRepositoryMock.deleteById(savedCourseId) } just runs

        courseService.deleteCourse(savedCourseId)

        verify(exactly = 1) {
            courseRepositoryMock.existsById(savedCourseId)
            courseRepositoryMock.deleteById(savedCourseId)
        }
    }

    @Test
    fun deleteCourseNotFound() {
        val savedCourse = course()
        val savedCourseId: Int = savedCourse.id!!

        every { courseRepositoryMock.existsById(savedCourseId) } returns false

        assertThatThrownBy {
            courseService.deleteCourse(savedCourseId)
        }.hasMessage("id not found")

        verify(exactly = 1) {
            courseRepositoryMock.existsById(savedCourseId)
        }
    }
}