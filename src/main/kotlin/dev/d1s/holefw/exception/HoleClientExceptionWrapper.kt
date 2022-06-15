package dev.d1s.holefw.exception

import dev.d1s.hole.client.exception.HoleClientException
import kotlin.system.measureTimeMillis

class HoleClientExceptionWrapper(
    val executionTime: Long,
    val holeClientException: HoleClientException
) : RuntimeException(holeClientException)

inline fun <R> withExceptionWrapping(block: () -> R): R {
    var result: R?

    var exception: HoleClientException? = null

    val executionTime = measureTimeMillis {
        result = try {
            block()
        } catch (e: HoleClientException) {
            exception = e
            null
        }
    }

    exception?.let {
        throw HoleClientExceptionWrapper(executionTime, it)
    }

    return result!!
}