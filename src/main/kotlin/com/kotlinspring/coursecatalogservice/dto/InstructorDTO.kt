package com.kotlinspring.coursecatalogservice.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class InstructorDTO(
    @JsonProperty("id") val id: Int?,
    @get:NotBlank(message = "InstructorDTO.name must not be blank")
    @JsonProperty("name") var name: String
)
