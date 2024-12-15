package com.noxis.database.di

import android.content.Context
import androidx.room.Room
import com.noxis.database.dao.NoteDao
import com.noxis.database.db.NoteDatabase
import com.noxis.database.db.NoteRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext appContext: Context): NoteDatabase {
        val db = Room.databaseBuilder(
            appContext,
            NoteRoomDatabase::class.java,
            NoteRoomDatabase.DB_NAME
        ).build()

        return NoteDatabase(db)
    }

    @Provides
    fun provideChannelDao(database: NoteDatabase): NoteDao {
        return database.noteDao
    }

}