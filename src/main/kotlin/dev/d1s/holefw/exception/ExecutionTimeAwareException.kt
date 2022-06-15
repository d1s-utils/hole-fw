package dev.d1s.holefw.exception

import org.springframework.http.HttpStatus
import kotlin.properties.Delegates
import kotlin.system.measureTimeMillis

open class ExecutionTimeAwareException(
    message: String? = null,
    cause: Throwable? = null,

    val status: HttpStatus? = null
) :
    RuntimeException(message, cause) {

    var executionTime by Delegates.notNull<Long>()
}

inline fun <R> withExceptionWrapping(block: () -> R): R {
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
        if (it is ExecutionTimeAwareException) {
            it.executionTime = executionTime
        }

        throw it
    }

    return result!!
}