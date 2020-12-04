package com.nikolam.feature_books.domain.model

import com.nikolam.feature_books.data.model.BookContent
import com.nikolam.feature_books.data.model.BookMetaData
import com.nikolam.feature_books.data.model.BookSettings
import java.util.*

data class BookDomainModel(
    val bid: UUID,
    val content: BookContent,
    val metaData: BookMetaData,
) {

    init {
        require(content.id == bid) { "wrong book content" }
        require(metaData.id == bid) { "Wrong metaData for $this" }
    }

    val type = metaData.type
    val author = metaData.author
    val name = metaData.name
    val root = metaData.root

    fun updateMetaData(update: BookMetaData.() -> BookMetaData): BookDomainModel = copy(
        metaData = update(metaData)
    )

    val coverTransitionName = "bookCoverTransition_$bid"

    inline fun updateContent(update: BookContent.() -> BookContent): BookDomainModel = copy(
        content = update(content)
    )

    inline fun update(
        updateContent: BookContent.() -> BookContent = { this },
        updateMetaData: BookMetaData.() -> BookMetaData = { this },
        updateSettings: BookSettings.() -> BookSettings = { this },
    ): BookDomainModel {
        val newSettings = updateSettings(content.settings)
        val contentWithNewSettings = if (newSettings === content.settings) {
            content
        } else {
            content.copy(
                settings = newSettings
            )
        }
        val newContent = updateContent(contentWithNewSettings)
        val newMetaData = updateMetaData(metaData)
        return copy(content = newContent, metaData = newMetaData)
    }

    companion object {
        const val SPEED_MAX = 2.5F
        const val SPEED_MIN = 0.5F
    }
}