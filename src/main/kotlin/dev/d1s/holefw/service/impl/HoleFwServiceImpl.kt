package dev.d1s.holefw.service.impl

import com.jakewharton.picnic.TableDsl
import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table
import dev.d1s.hole.client.core.HoleClient
import dev.d1s.hole.client.entity.storageObject.StorageObject
import dev.d1s.holefw.service.HoleFwService
import dev.d1s.teabag.stdlib.text.padding
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.system.measureTimeMillis

@Service
class HoleFwServiceImpl : HoleFwService {

    @set:Autowired
    lateinit var holeClient: HoleClient

    override fun getAvailableDirectories(): String {
        var availableGroups: Set<String>

        val time = runBlocking {
            measureTimeMillis {
                availableGroups = holeClient.getAvailableGroups()
            }
        }

        return renderTable {
            header {
                row("Available groups")
            }

            availableGroups.forEach {
                row(it)
            }

            if (availableGroups.isEmpty()) {
                row("There are no groups available yet.")
            }
        }.appendExecutionTime(time)
    }

    override fun getObjectsByGroup(group: String): String {
        var objects: Set<StorageObject>

        val time = runBlocking {
            measureTimeMillis {
                objects = holeClient.getAllObjects(group)
            }
        }

        return buildString {
            objects.forEach {
                val renderedText = renderTable {
                    row("ID", it.id)

                    row("Created at", it.creationTime)

                    row("Name", it.name)

                    row(
                        "Encrypted",
                        if (it.encrypted) {
                            "Yes"
                        } else {
                            "No"
                        }
                    )

                    row(
                        "Digest",
                        it.digest.short()
                    )

                    row(
                        "Last access",
                        it.accesses.firstOrNull()?.time ?: NO_VALUE
                    )

                    if (objects.isEmpty()) {
                        row {
                            repeat(
                                COLUMNS
                            ) {
                                cell(NO_VALUE)
                            }
                        }
                    }
                }

                append(renderedText)

                if (it != objects.last()) {
                    append(
                        SEPARATOR.repeat(
                            renderedText.lines().maxOf { line ->
                                line.length
                            }
                        ).padding {
                            top = BASE_PADDING
                            bottom = BASE_PADDING
                        }
                    )
                }
            }

            if (objects.isEmpty()) {
                append(
                    renderTable {
                        row()
                        row("There are no objects available yet.")
                    }
                )
            }
        }.appendExecutionTime(time)
    }

    fun String.short() = this.takeLast(SHORT_VALUE_LENGTH)

    private fun String.appendExecutionTime(time: Long) = this + renderTable(true) {
        row("Fetched in $time ms.")
    }

    private fun renderTable(adjustBottom: Boolean = false, block: TableDsl.() -> Unit) = table {
        cellStyle {
            paddingLeft = CELL_PADDING
            paddingRight = CELL_PADDING
            alignment = TextAlignment.MiddleLeft
        }

        block()
    }.renderText().setPadding(adjustBottom)

    private fun String.setPadding(adjustBottom: Boolean) = this.padding {
        top = BASE_PADDING

        bottom = BASE_PADDING + if (adjustBottom) {
            1
        } else {
            0
        }

        left = BASE_PADDING

        right = BASE_PADDING
    }

    private companion object {
        private const val BASE_PADDING = 1

        private const val CELL_PADDING = 1

        private const val SHORT_VALUE_LENGTH = 8

        private const val COLUMNS = 7

        private const val NO_VALUE = "-"

        private const val SEPARATOR = "-"
    }
}