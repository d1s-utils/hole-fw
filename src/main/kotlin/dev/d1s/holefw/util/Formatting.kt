package dev.d1s.holefw.util

import dev.d1s.holefw.constant.PADDING
import dev.d1s.holefw.constant.SEPARATOR
import dev.d1s.teabag.stdlib.text.padding
import kotlin.properties.Delegates

class ExecutionTimeContainer {
    var executionTime by Delegates.notNull<Long>()
}

inline fun buildResponse(block: StringBuilder.(ExecutionTimeContainer) -> Unit) = buildString {
    val executionTimeContainer = ExecutionTimeContainer()

    block(executionTimeContainer)

    appendSeparator()

    append("Done in ${executionTimeContainer.executionTime}ms")
}.padding {
    top = PADDING

    bottom = PADDING + 1

    left = PADDING

    right = PADDING
}

fun StringBuilder.appendSeparator() {
    append(
        SEPARATOR.repeat(
            toString().lines().maxOf { line ->
                line.length
            }
        ).padding {
            top = PADDING + 1
            bottom = PADDING + 1
        }
    )
}