package com.kotlinspring.coursecatalogservice.controller

import com.kotlinspring.coursecatalogservice.exception.CourseNotFoundException
import com.kotlinspring.coursecatalogservice.exception.InstructorNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLException
import java.util.stream.Collectors


@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): Map<String, List<String?>> {
        val errors = ex.bindingResult.fieldErrors
            .stream().map { obj: FieldError -> obj.defaultMessage }.collect(Collectors.toList())
        return getErrorsMap(errors)
    }

    @ExceptionHandler(CourseNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleCourseNotFoundException(exception: CourseNotFoundException) = exception.message

    @ExceptionHandler(InstructorNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleInstructorNotFoundException(exception: InstructorNotFoundException) =
        exception.message

    @ExceptionHandler(SQLException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleSQLException(exception: SQLException) = "Internal error: please contact support"

    private fun getErrorsMap(errors: List<String?>): Map<String, List<String?>> {
        val errorResponse: MutableMap<String, List<String?>> = HashMap()
        errorResponse["errors"] = errors
        return errorResponse
    }

}