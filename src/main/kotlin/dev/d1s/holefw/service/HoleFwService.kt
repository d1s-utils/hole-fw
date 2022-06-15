package dev.d1s.holefw.service

import dev.d1s.hole.client.entity.storageObject.RawStorageObject
import java.io.OutputStream

interface HoleFwService {

    fun getAvailableDirectories(): String

    fun getObjectsByGroup(group: String): String

    fun readRawObject(
        group: String,
        id: String,
        encryptionKey: String?,
        out: OutputStream
    ): RawStorageObject
}