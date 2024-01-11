package com.kotlinspring.coursecatalogservice.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class CourseDTO @JsonCreator constructor(
    @JsonProperty("id") val id: Int?,
    @get:NotBlank(message = "CourseDTO.name must not be blank")
    @JsonProperty("name") val name: String,
    @get:NotBlank(message = "CourseDTO.category must not be blank")
    @JsonProperty("category") val category: String
)