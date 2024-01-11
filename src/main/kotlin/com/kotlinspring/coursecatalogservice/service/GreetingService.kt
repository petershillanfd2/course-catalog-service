package com.kotlinspring.coursecatalogservice.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GreetingService(@Value("\${message}") val message: String) {
    fun retrieveGreeting(name: String): String = "$name, $message"
}