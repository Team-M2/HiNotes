package com.huawei.references.hinotes.data.ml

import com.huawei.references.hinotes.data.ml.abstractions.ImageTextRecognizer
import com.huawei.references.hinotes.data.ml.himlkitdatasource.ImageTextRecognizerHiImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val mlDataModule= module {
    factory {
        ImageTextRecognizerHiImpl()
    } bind ImageTextRecognizer::class

    factory {
        MLRepository(get())
    }
}