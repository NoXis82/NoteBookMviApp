package com.noxis.notes.data.di

import com.noxis.database.dao.NoteDao
import com.noxis.notes.data.repository.NoteRepositoryImpl
import com.noxis.notes.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    @Singleton
    fun provideNoteRepository(
        noteDao: NoteDao
    ): NoteRepository {
        return NoteRepositoryImpl(noteDao)
    }

}