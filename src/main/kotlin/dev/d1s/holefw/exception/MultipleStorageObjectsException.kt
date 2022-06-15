package dev.d1s.holefw.exception

import dev.d1s.holefw.constant.MULTIPLE_STORAGE_OBJECTS_ERROR
import org.springframework.http.HttpStatus

class MultipleStorageObjectsException :
    ExecutionTimeAwareException(
        MULTIPLE_STORAGE_OBJECTS_ERROR,
        status = HttpStatus.UNPROCESSABLE_ENTITY
    )