package com.huawei.references.hinotes

import android.app.Application
import com.huawei.references.hinotes.data.dataModule
import com.huawei.references.hinotes.ui.uiModule
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
