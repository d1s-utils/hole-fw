package dev.d1s.holefw.controller.advice

import dev.d1s.holefw.exception.HoleClientExceptionWrapper
import dev.d1s.holefw.util.buildResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class HoleClientExceptionWrapperHandler {

    @ExceptionHandler
    fun handleClientException(e: HoleClientExceptionWrapper): ResponseEntity<String> {
        val holeError = e.holeClientException.error

        // since we act as a wrapper, we just proxy the status code to the client
        return status(holeError?.status ?: HttpStatus.INTERNAL_SERVER_ERROR.value())
            .contentType(MediaType.TEXT_PLAIN)
            .body(
                buildResponse {
                    it.executionTime = e.executionTime

                    append(holeError?.error ?: e.message ?: "An error occurred.")
                }
            )
    }
}