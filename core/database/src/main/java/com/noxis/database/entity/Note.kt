package com.noxis.database.entity

import androidx.compose.ui.text.AnnotatedString
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime


@Entity("notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: AnnotatedString,
    val encrypt: Boolean = false,
    val password: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @ColumnInfo(name = "modified_at")
    val modifiedAt: OffsetDateTime
)
