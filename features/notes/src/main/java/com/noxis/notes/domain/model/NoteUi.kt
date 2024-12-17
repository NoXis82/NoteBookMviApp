package com.noxis.notes.domain.model

import androidx.compose.ui.text.AnnotatedString
import java.time.OffsetDateTime

data class NoteUi(
    val id: Long = 0,
    val title: String,
    val description: AnnotatedString,
    val encrypt: Boolean = false,
    val password: String? = null,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val modifiedAt: OffsetDateTime
)
