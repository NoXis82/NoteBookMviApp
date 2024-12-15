package com.noxis.notes.presentation.state

import androidx.annotation.StringRes
import com.noxis.notes.domain.model.NoteUi


data class NoteUiViewState(
    val isLoading: Boolean = false,
    val notes: List<NoteUi> = emptyList(),
    val selectedNote: NoteUi? = null,
    val showPasswordSheet: Boolean = false,
    @StringRes val passwordErrorResId: Int? = null
)
