package com.kotlinspring.coursecatalogservice.dto

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

data class InstructorDTO(
    val id: Int?,
    var name: String
)
