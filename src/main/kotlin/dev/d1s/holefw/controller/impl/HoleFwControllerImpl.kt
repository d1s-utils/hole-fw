package dev.d1s.holefw.controller.impl

import dev.d1s.holefw.controller.HoleFwController
import dev.d1s.holefw.service.HoleFwService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
class HoleFwControllerImpl : HoleFwController {

    @set:Autowired
    lateinit var holeFwService: HoleFwService

    override fun getAvailableDirectories(): ResponseEntity<String> = ok(
        holeFwService.getAvailableGroups()
    )

    override fun getObjectsByGroup(group: String): ResponseEntity<String> = ok(
        holeFwService.getObjectsByGroup(group)
    )

    override fun readRawObject(
        group: String,
        id: String,
        encryptionKey: String?,
        response: HttpServletResponse,
    ) {
        holeFwService.readRawObject(
            group,
            id,
            encryptionKey,
            response
        )
    }
}