package com.noxis.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noxis.notes.R
import com.noxis.notes.domain.model.NoteUi
import com.noxis.notes.domain.repository.NoteRepository
import com.noxis.notes.presentation.event.EventManager
import com.noxis.notes.presentation.intent.NoteIntent
import com.noxis.notes.presentation.state.NoteUiViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: Provider<NoteRepository>
) : ViewModel() {

    /**
     * A private MutableStateFlow to store and update the UI state for the notes screen.
     */
    private val _notesViewState = MutableStateFlow(NoteUiViewState())
    val notesViewState: StateFlow<NoteUiViewState> = _notesViewState

    /**
     * Upon creation, the ViewModel immediately triggers a LoadNotes
     * intent to fetch and display the current user list. This automatic
     * loading of data when the ViewModel is initialized ensures that the
     * UI is always populated with up-to-date information when the notes
     * screen appears.
     */
    init {
        handleIntent(NoteIntent.LoadNotes)
    }

    /**
     * Handles user intents by mapping them to appropriate methods.
     * This ensures that all user actions are processed in a centralized, predictable manner.
     *
     * @param intent The user action to be handled.
     */
    fun handleIntent(intent: NoteIntent) {
        when (intent) {
            is NoteIntent.LoadNotes -> loadNotes()
            is NoteIntent.DeleteNote -> deleteNote(intent.note)
            is NoteIntent.OpenNoteClicked -> onNoteClicked(intent.note)
            is NoteIntent.ValidatePassword -> validatePassword(intent.password)
            else -> { /* No-op for unsupported intents */
            }
        }
    }

    /**
     * The loadNotes function performs the following steps:
     * 1. Set Loading State: Updates the state to indicate that a loading process
     *     is in progress by setting isLoading = true.
     * 2.  Retrieve Data: Using viewModelScope.launch, it asynchronously fetches
     *     the list of notes from the repository.
     * 3.  Update State: Once the data is fetched, the state is updated with the
     *     new notes list, and the loading flag is set to false to indicate that
     *     the loading process is complete.
     * 4.  Trigger Effect: A snackbar effect is sent to notify the user that the
     *     data has been successfully loaded.
     * This function ensures that the UI accurately reflects the loading status and the
     * notes list, following the MVI pattern’s principles of immutability and
     * state management.
     */
    private fun loadNotes() {
        viewModelScope.launch(IO) {
            repository.get().getNotes()
                .onStart {
                    _notesViewState.value = _notesViewState.value.copy(isLoading = true)
                }
                .collect { notes ->
                    _notesViewState.value = _notesViewState.value.copy(
                        isLoading = false,
                        notes = notes
                    )
                }
        }
    }

    /**
     * Deletes a note via the repository and triggers a snackbar event to notify the user of success.
     *
     * @param note The note to be deleted.
     */
    private fun deleteNote(note: NoteUi) {
        viewModelScope.launch(IO) {
            repository.get().deleteNote(note)
            EventManager.triggerEvent(EventManager.AppEvent.ShowSnackbar(R.string.delete_note_success))
        }
    }

    /**
     * Handles the user clicking on a note.
     * If the note is encrypted, prompts the user to enter a password.
     * Otherwise, navigates to the note detail screen.
     *
     * @param note The note that was clicked.
     */
    private fun onNoteClicked(note: NoteUi) {
        if (note.encrypt) {
            _notesViewState.value = _notesViewState.value.copy(
                selectedNote = note,
                showPasswordSheet = true
            )
        } else {
            EventManager.triggerEvent(EventManager.AppEvent.NavigateToDetail(note.id))
        }
    }

    private fun validatePassword(password: String) {
        _notesViewState.value.selectedNote?.let { note ->
            if (password != note.password) {
                _notesViewState.value = _notesViewState.value.copy(
                    passwordErrorResId = R.string.error_password
                )
            } else {
                _notesViewState.value = _notesViewState.value.copy(showPasswordSheet = false)
                EventManager.triggerEvent(EventManager.AppEvent.NavigateToDetail(note.id))
            }
        }
    }

}