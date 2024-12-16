package com.noxis.notes.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noxis.notes.R
import com.noxis.notes.domain.repository.NoteRepository
import com.noxis.notes.presentation.event.EventManager
import com.noxis.notes.presentation.intent.NoteIntent
import com.noxis.notes.presentation.state.NoteDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: Provider<NoteRepository>
) : ViewModel() {

    // "noteId" is nullable because noteId can be null when adding a new note
    private val noteId: String? = savedStateHandle["noteId"]

    // This is a mutable state flow that will be used internally in the viewmodel,
    private val _noteDetailViewState = MutableStateFlow(
        NoteDetailUiState(showDeleteIcon = noteId != null)
    )

    // Immutable state flow that is exposed to the UI
    val noteDetailViewState: StateFlow<NoteDetailUiState> = _noteDetailViewState

    init {
        handleIntent(NoteIntent.LoadNote)
    }

    fun handleIntent(intent: NoteIntent) {
        when (intent) {
            is NoteIntent.LoadNote -> loadNote()
            is NoteIntent.UpdateNoteTitle -> updateNoteTitle(intent.title)
            is NoteIntent.UpdateNoteDescription -> updateNoteDesc(intent.description)
            is NoteIntent.AddOrSaveNote -> noteId?.let { saveNote() } ?: addNote()
            is NoteIntent.DeleteNote -> deleteNote()
            is NoteIntent.LockNote -> lockNote(intent.password)
            is NoteIntent.UnLockNote -> unlockNote()
            else -> {}
        }
    }


    private fun loadNote() = viewModelScope.launch {
        noteId?.let { noteId ->
            repository.get().getNote(noteId.toLong()).filterNotNull().collect { note ->
                _noteDetailViewState.value = _noteDetailViewState.value.copy(note = note)
            }
        }
    }

    private fun updateNoteTitle(title: String) {
        val note = _noteDetailViewState.value.note.copy(title = title)
        _noteDetailViewState.value = _noteDetailViewState.value.copy(
            note = note,
            showSaveIcon = true
        )
    }

    private fun updateNoteDesc(desc: String) {
        _noteDetailViewState.value = _noteDetailViewState.value.copy(
            note = _noteDetailViewState.value.note.copy(description = desc),
            showSaveIcon = true
        )
    }

    private fun lockNote(password: String) {
        _noteDetailViewState.value = _noteDetailViewState.value.copy(
            note = _noteDetailViewState.value.note.copy(encrypt = true, password = password),
            showSaveIcon = true
        )
    }

    private fun unlockNote() {
        _noteDetailViewState.value = _noteDetailViewState.value.copy(
            note = _noteDetailViewState.value.note.copy(encrypt = false, password = null),
            showSaveIcon = true
        )
    }

    fun addNote() {
        viewModelScope.launch(IO) {
            val note = _noteDetailViewState.value.note.copy(
                createdAt = OffsetDateTime.now(),
                modifiedAt = OffsetDateTime.now()
            )
            repository.get().insertNote(note)
        }
        EventManager.triggerEvent(EventManager.AppEvent.ShowSnackbar(R.string.add_note_success))
        EventManager.triggerEventWithDelay(EventManager.AppEvent.ExitScreen)
    }

    fun saveNote() {
        viewModelScope.launch(IO) {
            val updatedNote = _noteDetailViewState.value.note.copy(
                modifiedAt = OffsetDateTime.now()
            )
            repository.get().updateNote(updatedNote)
        }
        EventManager.triggerEvent(EventManager.AppEvent.ShowSnackbar(R.string.update_note_success))
        EventManager.triggerEventWithDelay(EventManager.AppEvent.ExitScreen)
    }

    private fun deleteNote() {
        viewModelScope.launch(IO) {
            val note = _noteDetailViewState.value.note
            repository.get().deleteNote(note)
        }
        EventManager.triggerEvent(EventManager.AppEvent.ShowSnackbar(R.string.delete_note_success))
        EventManager.triggerEventWithDelay(EventManager.AppEvent.ExitScreen)
    }
}