package dev.d1s.holefw.exception

import org.springframework.http.HttpStatus
import kotlin.properties.Delegates

open class ExecutionTimeAwareException(
    message: String? = null,
    cause: Throwable? = null,

    val status: HttpStatus? = null,
) :
    RuntimeException(message, cause) {

    var executionTime by Delegates.notNull<Long>()
}