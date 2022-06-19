package dev.d1s.holefw.util

import dev.d1s.hole.client.exception.HoleClientException
import dev.d1s.holefw.exception.ExecutionTimeAwareException
import dev.d1s.holefw.exception.HoleClientExceptionWrapper
import kotlin.system.measureTimeMillis

inline fun <R> withExceptionHandling(block: () -> R): R {
    var result: R?

    var exception: Exception? = null

    val executionTime = measureTimeMillis {
        result = try {
            block()
        } catch (e: Exception) {
            exception = e
            null
        }
    }

    exception?.let {
        val adjustedException = if (it is HoleClientException) {
            HoleClientExceptionWrapper(it)
        } else {
            it
        }

        if (adjustedException is ExecutionTimeAwareException) {
            adjustedException.executionTime = executionTime
        }

        throw adjustedException
    }

    return result!!
}