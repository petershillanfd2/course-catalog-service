package com.kotlinspring.coursecatalogservice.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val MESSAGE = "Hello"
private const val NAME = "Peter"

class GreetingServiceTest {
    private val greetingService = GreetingService(MESSAGE)

    @Test
    fun retrieveGreeting() {
        assertThat(greetingService.retrieveGreeting(NAME)).isEqualTo("$NAME, $MESSAGE")
    }
}