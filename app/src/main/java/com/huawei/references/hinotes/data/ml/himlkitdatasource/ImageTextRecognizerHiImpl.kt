package com.huawei.references.hinotes.data.ml.himlkitdatasource

import android.graphics.Bitmap
import com.huawei.hmf.tasks.Task
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.text.MLLocalTextSetting
import com.huawei.hms.mlsdk.text.MLText
import com.huawei.references.hinotes.data.DataConstants.Companion.IMAGE_RECOGNITION_LANGUAGE
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.ml.abstractions.ImageTextRecognizer
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageTextRecognizerHiImpl : ImageTextRecognizer {
    override suspend fun recognizeImage(bitmap: Bitmap): DataHolder<String> {
        val setting = MLLocalTextSetting.Factory()
            .setOCRMode(MLLocalTextSetting.OCR_DETECT_MODE) // Specify languages that can be recognized.
            .setLanguage(IMAGE_RECOGNITION_LANGUAGE)
            .create()
        val analyzer = MLAnalyzerFactory.getInstance().getLocalTextAnalyzer(setting)
        val frame = MLFrame.fromBitmap(bitmap)
        val task: Task<MLText> = analyzer.asyncAnalyseFrame(frame)
        return suspendCoroutine { cont ->
            task.addOnSuccessListener {
                if (it.stringValue.isNullOrEmpty()) {
                    cont.resume(DataHolder.Fail())
                } else {
                    cont.resume(DataHolder.Success(it.stringValue.replace("\n", " ")))
                }
            }.addOnFailureListener {
                cont.resume(DataHolder.Fail(errStr = it.message?:""))
            }
        }

    }
}