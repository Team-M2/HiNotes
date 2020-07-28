package com.huawei.references.hinotes.data.ml

import android.graphics.Bitmap
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.ml.abstractions.ImageTextRecognizer

class MLRepository(private val imageTextRecognizer: ImageTextRecognizer) {

    suspend fun recognizeImage(bitmap: Bitmap) : DataHolder<String> =
        imageTextRecognizer.recognizeImage(bitmap)
}