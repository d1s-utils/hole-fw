package dev.d1s.holefw.controller.advice

import dev.d1s.hole.client.exception.HoleClientException
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.internalServerError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class HoleClientExceptionHandler {

    @ExceptionHandler
    fun handleClientException(e: HoleClientException): ResponseEntity<String> =
        internalServerError().body(e.message)
}