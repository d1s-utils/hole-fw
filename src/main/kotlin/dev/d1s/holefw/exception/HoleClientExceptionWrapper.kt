package dev.d1s.holefw.exception

import dev.d1s.hole.client.exception.HoleClientException

class HoleClientExceptionWrapper(
    val holeClientException: HoleClientException
) : ExecutionTimeAwareException(cause = holeClientException)