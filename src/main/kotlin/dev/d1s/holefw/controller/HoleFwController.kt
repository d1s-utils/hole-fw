package dev.d1s.holefw.controller

import dev.d1s.holefw.constant.GET_AVAILABLE_DIRECTORIES_MAPPING
import dev.d1s.holefw.constant.GET_OBJECTS_BY_GROUP_MAPPING
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import javax.validation.constraints.NotBlank

@Validated
interface HoleFwController {

    @GetMapping(GET_AVAILABLE_DIRECTORIES_MAPPING, produces = [MediaType.TEXT_PLAIN_VALUE])
    fun getAvailableDirectories(): ResponseEntity<String>

    @GetMapping(GET_OBJECTS_BY_GROUP_MAPPING, produces = [MediaType.TEXT_PLAIN_VALUE])
    fun getObjectsByGroup(@PathVariable @NotBlank group: String): ResponseEntity<String>
}