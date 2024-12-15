package com.noxis.notes.data.mapper

import com.noxis.database.entity.Note
import com.noxis.notes.domain.model.NoteUi

fun Note.mapToNoteUi(): NoteUi {
    return NoteUi(
        id = this.id,
        title = this.title,
        description = this.description,
        encrypt = this.encrypt,
        password = this.password,
        createdAt = this.createdAt,
        modifiedAt = this.modifiedAt
    )
}

fun NoteUi.mapToNoteDao(): Note {
    return Note(
        id = this.id,
        title = this.title,
        description = this.description,
        encrypt = this.encrypt,
        password = this.password,
        createdAt = this.createdAt,
        modifiedAt = this.modifiedAt
    )
}