package dev.d1s.holefw.service

import javax.servlet.http.HttpServletResponse

interface HoleFwService {

    fun getAvailableGroups(): String

    fun getObjectsByGroup(group: String): String

    fun readRawObject(
        group: String,
        id: String,
        encryptionKey: String?,
        content: HttpServletResponse,
    )
}