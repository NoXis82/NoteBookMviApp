package com.noxis.notes.domain.repository

import com.noxis.database.entity.Note
import com.noxis.notes.domain.model.NoteUi
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(note: NoteUi)
    suspend fun updateNote(note: NoteUi)
    suspend fun deleteNote(note: NoteUi)
    fun getNotes(): Flow<List<NoteUi>>
    fun getNote(noteId: Long): Flow<NoteUi>
}