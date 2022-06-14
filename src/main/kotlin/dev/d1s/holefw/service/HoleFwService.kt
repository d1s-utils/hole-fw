package dev.d1s.holefw.service

interface HoleFwService {

    fun getAvailableDirectories(): String

    fun getObjectsByGroup(group: String): String
}