package com.kotlinspring.coursecatalogservice.repository

import com.kotlinspring.coursecatalogservice.entity.Instructor
import org.springframework.data.repository.CrudRepository

interface InstructorRepository : CrudRepository<Instructor, Int> {

}
