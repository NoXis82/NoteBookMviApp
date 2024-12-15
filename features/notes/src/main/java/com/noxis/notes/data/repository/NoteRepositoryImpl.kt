package com.noxis.notes.data.repository

import com.noxis.database.dao.NoteDao
import com.noxis.notes.data.mapper.mapToNoteDao
import com.noxis.notes.data.mapper.mapToNoteUi
import com.noxis.notes.domain.model.NoteUi
import com.noxis.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {


    override suspend fun insertNote(note: NoteUi) {
        noteDao.insertNote(note.mapToNoteDao())
    }

    override suspend fun updateNote(note: NoteUi) {
        noteDao.updateNote(note.mapToNoteDao())
    }

    override suspend fun deleteNote(note: NoteUi) {
        noteDao.deleteNote(note.mapToNoteDao())
    }

    override fun getNotes(): Flow<List<NoteUi>> {
        return flow {
            emit(noteDao.fetchAllNotes().map {
                it.mapToNoteUi()
            }
            )
        }
    }

    override fun getNote(noteId: Long): Flow<NoteUi> {
        return flow {
           emit(noteDao.getNote(noteId).mapToNoteUi())
        }
    }
}