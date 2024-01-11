package com.kotlinspring.coursecatalogservice.repository

import com.kotlinspring.coursecatalogservice.entity.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int> {
    fun findByNameContaining(name: String): List<Course>
}