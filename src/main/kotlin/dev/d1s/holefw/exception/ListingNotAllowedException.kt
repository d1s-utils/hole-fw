package dev.d1s.holefw.exception

import dev.d1s.holefw.constant.LISTING_NOT_ALLOWED_ERROR
import org.springframework.http.HttpStatus

class ListingNotAllowedException : ExecutionTimeAwareException(
    LISTING_NOT_ALLOWED_ERROR,
    status = HttpStatus.FORBIDDEN
)