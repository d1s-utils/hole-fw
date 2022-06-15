package dev.d1s.holefw.controller.advice

import dev.d1s.holefw.exception.ExecutionTimeAwareException
import dev.d1s.holefw.exception.HoleClientExceptionWrapper
import dev.d1s.holefw.util.buildResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    fun handleClientException(e: Exception): ResponseEntity<String> = when (e) {
        is HoleClientExceptionWrapper -> {
            val holeError = e.holeClientException.error

            this.errorResponse(
                // since we act as a wrapper, we just proxy the status code to the client
                holeError?.status ?: HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e,
                holeError?.error
            )
        }

        is ExecutionTimeAwareException -> {
            this.errorResponse(
                e.status?.value() ?: HttpStatus.BAD_REQUEST.value(),
                e
            )
        }

        else -> {
            throw e
        }
    }

    private fun errorResponse(
        status: Int,
        e: ExecutionTimeAwareException,
        message: String? = null
    ): ResponseEntity<String> = status(status)
        .contentType(MediaType.TEXT_PLAIN)
        .body(
            buildResponse {
                it.executionTime = e.executionTime

                append(message ?: e.message ?: "An error occurred.")
            }
        )
}