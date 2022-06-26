package dev.d1s.holefw.util

import dev.d1s.holefw.constant.PADDING
import dev.d1s.holefw.constant.SEPARATOR
import dev.d1s.teabag.stdlib.text.padding
import kotlin.properties.Delegates

class ResponseMetadataContainer {
    var executionTime: Long by Delegates.notNull()
    var hint: String? = null
}

inline fun buildResponse(block: StringBuilder.(ResponseMetadataContainer) -> Unit) = buildString {
    val responseMetadataContainer = ResponseMetadataContainer()

    block(responseMetadataContainer)

    appendSeparator()

    appendLine("Done in ${responseMetadataContainer.executionTime}ms")

    responseMetadataContainer.hint?.let {
        appendLine()

        append("Hint: $it")
    }
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