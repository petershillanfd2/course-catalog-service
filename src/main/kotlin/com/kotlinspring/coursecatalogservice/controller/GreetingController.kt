package com.kotlinspring.coursecatalogservice.controller

import com.kotlinspring.coursecatalogservice.service.GreetingService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/greetings")
class GreetingController(val greetingService: GreetingService) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/{name}")
    fun retrieveGreeting(@PathVariable name: String): String {
        logger.info { "retrieveGreeting() name is $name" }
        return greetingService.retrieveGreeting(name)
    }
}