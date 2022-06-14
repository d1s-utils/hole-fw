package dev.d1s.holefw.controller.impl

import dev.d1s.holefw.controller.HoleFwController
import dev.d1s.holefw.service.HoleFwService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController

@RestController
class HoleFwControllerImpl : HoleFwController {

    @set:Autowired
    lateinit var holeFwService: HoleFwService

    override fun getAvailableDirectories(): ResponseEntity<String> = ok(
        holeFwService.getAvailableDirectories()
    )

    override fun getObjectsByGroup(group: String): ResponseEntity<String> = ok(
        holeFwService.getObjectsByGroup(group)
    )
}