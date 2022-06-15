package dev.d1s.holefw.service.impl

import com.jakewharton.picnic.TableDsl
import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table
import dev.d1s.hole.client.core.HoleClient
import dev.d1s.hole.client.entity.storageObject.RawStorageObject
import dev.d1s.hole.client.entity.storageObject.StorageObject
import dev.d1s.holefw.constant.CELL_PADDING_RIGHT
import dev.d1s.holefw.constant.NO_VALUE
import dev.d1s.holefw.constant.SHORT_VALUE_LENGTH
import dev.d1s.holefw.exception.withExceptionWrapping
import dev.d1s.holefw.service.HoleFwService
import dev.d1s.holefw.util.appendSeparator
import dev.d1s.holefw.util.buildResponse
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.OutputStream
import kotlin.system.measureTimeMillis

@Service
class HoleFwServiceImpl : HoleFwService {

    @set:Autowired
    lateinit var holeClient: HoleClient

    override fun getAvailableDirectories(): String = buildResponse {
        var availableGroups: Set<String>

        it.executionTime = runBlocking {
            measureTimeMillis {
                availableGroups = withExceptionWrapping {
                    holeClient.getAvailableGroups()
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
                objects = withExceptionWrapping {
                    holeClient.getAllObjects(group)
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

    override fun readRawObject(id: String, encryptionKey: String?, out: OutputStream): RawStorageObject =
        withExceptionWrapping {
            runBlocking {
                holeClient.getRawObject(id, out, encryptionKey)
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
}