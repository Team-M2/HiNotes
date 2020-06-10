package com.huawei.references.hinotes.data.base

import com.huawei.references.hinotes.data.DataConstants.Companion.DEFAULT_ERROR_STR
import com.huawei.references.hinotes.data.DataConstants.Companion.DEFAULT_LOADING_STR

sealed class DataHolder<out T: Any> {

    data class Success<out T : Any>(val data:T) : DataHolder<T>()

    data class Fail(val errorResourceId : Int?=null,
                    val errStr: String=DEFAULT_ERROR_STR
    ) : DataHolder<Nothing>()

    data class Loading(val loadingResourceId : Int?=null,
                       val loadingStr: String=DEFAULT_LOADING_STR
                       ) : DataHolder<Nothing>()

}