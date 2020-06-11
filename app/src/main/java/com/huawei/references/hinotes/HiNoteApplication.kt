package com.huawei.references.hinotes

import android.app.Activity
import android.app.Application
import com.huawei.references.hinotes.data.dataModule
import com.huawei.references.hinotes.data.note.noteDataModule
import com.huawei.references.hinotes.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HiNoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HiNoteApplication)
            modules(dataModule + uiModule)
        }
    }
}
