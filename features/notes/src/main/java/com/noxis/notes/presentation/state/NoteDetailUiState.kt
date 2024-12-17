package com.noxis.notes.presentation.state

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import com.noxis.notes.domain.model.NoteUi
import java.time.OffsetDateTime

@Immutable
data class NoteDetailUiState(
    val note: NoteUi = NoteUi(title = "", description = AnnotatedString(text = ""), modifiedAt = OffsetDateTime.now()),
    val showDeleteIcon: Boolean,
    val showSaveIcon: Boolean = false
)
