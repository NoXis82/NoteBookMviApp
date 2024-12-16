package com.noxis.notes.presentation.state

import androidx.compose.runtime.Immutable
import com.noxis.notes.domain.model.NoteUi
import java.time.OffsetDateTime

@Immutable
data class NoteDetailUiState(
    val note: NoteUi = NoteUi(title = "", description = "", modifiedAt = OffsetDateTime.now()),
    val showDeleteIcon: Boolean,
    val showSaveIcon: Boolean = false
)
