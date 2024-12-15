package com.noxis.notebookmviapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltAndroidApp
class NoteApp: Application() {
    @Inject
    @ApplicationContext
    lateinit var context: Context
}