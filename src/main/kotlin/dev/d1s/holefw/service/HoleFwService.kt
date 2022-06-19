package dev.d1s.holefw.service

import javax.servlet.http.HttpServletResponse

interface HoleFwService {

    fun getAvailableDirectories(): String

    fun getObjectsByGroup(group: String): String

    fun readRawObject(
        group: String,
        id: String,
        encryptionKey: String?,
        content: HttpServletResponse,
    )
}