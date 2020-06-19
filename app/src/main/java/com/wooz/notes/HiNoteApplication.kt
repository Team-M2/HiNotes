package com.wooz.notes

import android.app.Application
import com.wooz.notes.data.dataModule
import com.wooz.notes.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin

class HiNoteApplication : Application(),KoinComponent {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HiNoteApplication)
            modules(dataModule + uiModule)
        }

    }

}
