package com.huawei.references.hinotes.data.ml.abstractions

import android.graphics.Bitmap
import com.huawei.references.hinotes.data.base.DataHolder

interface ImageTextRecognizer {
    suspend fun recognizeImage(bitmap: Bitmap) : DataHolder<String>
}