package dev.d1s.holefw.service.impl

import com.jakewharton.picnic.TableDsl
import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table
import dev.d1s.hole.client.core.HoleClient
import dev.d1s.hole.client.entity.metadata.Meta
import dev.d1s.hole.client.entity.metadata.getValue
import dev.d1s.hole.client.entity.storageObject.RawStorageObject
import dev.d1s.hole.client.entity.storageObject.StorageObject
import dev.d1s.hole.client.exception.HoleClientException
import dev.d1s.holefw.constant.ALLOW_LISTING_PROPERTY
import dev.d1s.holefw.constant.CELL_PADDING_RIGHT
import dev.d1s.holefw.constant.NO_VALUE
import dev.d1s.holefw.constant.SHORT_VALUE_LENGTH
import dev.d1s.holefw.exception.ListingNotAllowedException
import dev.d1s.holefw.exception.MultipleStorageObjectsException
import dev.d1s.holefw.exception.StorageObjectNotFoundByNameException
import dev.d1s.holefw.service.HoleFwService
import dev.d1s.holefw.util.appendSeparator
import dev.d1s.holefw.util.buildResponse
import dev.d1s.holefw.util.withExceptionHandling
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletResponse
import kotlin.system.measureTimeMillis

@Service
class HoleFwServiceImpl : HoleFwService {

    @set:Autowired
    lateinit var holeClient: HoleClient

    override fun getAvailableGroups(): String = buildResponse {
        var availableGroups: Set<String>

        it.executionTime = runBlocking {
            measureTimeMillis {
                availableGroups = withExceptionHandling {
                    holeClient.getAllGroupNames()
                }
            }
        }

        append(
            renderTable {
                header {
                    row("Available groups")
                }

                availableGroups.forEach { group ->
                    row(group)
                }

                if (availableGroups.isEmpty()) {
                    row("There are no groups available yet.")
                }
            }
        )
    }

    override fun getObjectsByGroup(group: String): String = buildResponse {
        var objects: Set<StorageObject>

        it.executionTime = runBlocking {
            measureTimeMillis {
                objects = withExceptionHandling {
                    val foundGroup = holeClient.getGroup(group)

                    if (!foundGroup.metadata.allowsListing()) {
                        throw ListingNotAllowedException()
                    }

                    foundGroup.storageObjects.filter { obj ->
                        obj.metadata.allowsListing()
                    }.toSet()
                }
            }
        }


        objects.forEach { obj ->
            val renderedText = renderTable {
                rowWithIndent(
                    "ID",
                    obj.id
                )

                rowWithIndent(
                    "Created at",
                    obj.creationTime.toString()
                )

                rowWithIndent(
                    "Name",
                    obj.name
                )

                rowWithIndent(
                    "Encrypted",
                    if (obj.encrypted) {
                        "Yes"
                    } else {
                        "No"
                    }
                )

                rowWithIndent(
                    "Digest",
                    obj.digest.short()
                )

                rowWithIndent(
                    "Last access",
                    obj.accesses.firstOrNull()?.time?.toString() ?: NO_VALUE
                )
            }

            append(renderedText)

            if (obj != objects.last()) {
                appendSeparator()
            }
        }

        if (objects.isEmpty()) {
            append(
                renderTable {
                    row("There are no objects available yet.")
                }
            )
        }
    }

    override fun readRawObject(
        group: String,
        id: String,
        encryptionKey: String?,
        content: HttpServletResponse,
    ) {
        val rawStorageObjectHandler = fun(rawStorageObject: RawStorageObject) {
            content.contentType = rawStorageObject.contentType
            content.setContentLengthLong(rawStorageObject.contentLength)
        }

        withExceptionHandling {
            runBlocking {
                val out = content.outputStream

                try {
                    holeClient.getRawObject(id, out, encryptionKey, rawStorageObjectHandler)
                } catch (e: HoleClientException) {
                    holeClient.getRawObject(
                        holeClient.getGroup(group)
                            .storageObjects
                            .filter {
                                it.name == id
                            }.let {
                                if (it.isEmpty()) {
                                    throw StorageObjectNotFoundByNameException(id)
                                }

                                if (it.size > 1) {
                                    throw MultipleStorageObjectsException()
                                }

                                it.first().id
                            },
                        out,
                        encryptionKey,
                        rawStorageObjectHandler
                    )
                }
            }
        }
    }

    fun String.short() = this.takeLast(SHORT_VALUE_LENGTH)

    private fun renderTable(block: TableDsl.() -> Unit) = table {
        cellStyle {
            alignment = TextAlignment.MiddleLeft
        }

        block()
    }.renderText()

    private fun TableDsl.rowWithIndent(key: String, value: String) {
        row {
            cell(key) {
                paddingRight = CELL_PADDING_RIGHT
            }

            cell(value)
        }
    }

    private fun Meta.allowsListing() =
        this.getValue(ALLOW_LISTING_PROPERTY)?.equals("true") ?: true
}