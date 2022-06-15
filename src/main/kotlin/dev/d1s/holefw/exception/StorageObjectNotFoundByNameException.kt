package dev.d1s.holefw.exception

import dev.d1s.holefw.constant.STORAGE_OBJECT_NOT_FOUND_BY_NAME
import org.springframework.http.HttpStatus

class StorageObjectNotFoundByNameException(name: String) : ExecutionTimeAwareException(
    STORAGE_OBJECT_NOT_FOUND_BY_NAME.format(name),
    status = HttpStatus.NOT_FOUND
)