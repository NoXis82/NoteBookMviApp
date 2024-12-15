package com.noxis.notes.presentation.intent

import android.provider.ContactsContract
import androidx.compose.ui.text.AnnotatedString
import com.noxis.notes.domain.NoteUi

sealed class NoteIntent {
    data object LoadNotes : NoteIntent()
    data object LoadNote : NoteIntent()
    data object AddOrSaveNote : NoteIntent()
    data class UpdateNoteTitle(val title: String) : NoteIntent()
    data class UpdateNoteDescription(val description: AnnotatedString) : NoteIntent()
    data class LockNote(val password: String): NoteIntent()
    data object UnLockNote: NoteIntent()
    data class DeleteNote(val note: ContactsContract.CommonDataKinds.Note) : NoteIntent()
    data class OpenNoteClicked(val note: NoteUi): NoteIntent()
    data class ValidatePassword(val password: String): NoteIntent()
}