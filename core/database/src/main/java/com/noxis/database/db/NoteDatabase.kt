package com.noxis.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.noxis.database.dao.NoteDao
import com.noxis.database.entity.Note
import com.noxis.database.util.AnnotatedStringConverter
import com.noxis.database.util.TimestampConverter


class NoteDatabase internal constructor(private val database: NoteRoomDatabase) {
    val noteDao: NoteDao
        get() = database.noteDao()
}

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TimestampConverter::class, AnnotatedStringConverter::class)
internal abstract class NoteRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val DB_NAME = "notes_db"
    }
}