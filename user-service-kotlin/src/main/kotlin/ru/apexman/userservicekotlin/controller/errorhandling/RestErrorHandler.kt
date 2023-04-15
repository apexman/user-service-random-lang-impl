package ru.apexman.userservicekotlin.controller.errorhandling

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
class RestErrorHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(
        exception: ForbiddenException
    ): ResponseEntity<ErrorResponse> {
        logger.warn(exception.message, exception)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse(exception.message!!))
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(
        exception: NotFoundException
    ): ResponseEntity<ErrorResponse> {
        logger.warn(exception.message, exception)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(exception.message!!))
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(
        exception: BadRequestException
    ): ResponseEntity<ErrorResponse> {
        logger.warn(exception.message, exception)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(exception.message!!))
    }

    @ExceptionHandler(ConflictException::class)
    fun handleConflictException(
        exception: ConflictException
    ): ResponseEntity<ErrorResponse> {
        logger.warn(exception.message, exception)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse(exception.message!!))
    }

    @ExceptionHandler(Exception::class)
    fun handleAllUncaughtException(
        exception: Exception
    ): ResponseEntity<ErrorResponse> {
        val className = exception.javaClass.name
        val message = exception.message ?: ""
        val errorText = "$className: $message"
        logger.error(errorText, exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse(errorText))
    }
}
