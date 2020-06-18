package com.wooz.notes.data.base

import com.wooz.notes.data.DataConstants.Companion.DEFAULT_ERROR_STR
import com.wooz.notes.data.DataConstants.Companion.DEFAULT_LOADING_STR

sealed class DataHolder<out T: Any> {

    data class Success<out T : Any>(val data:T) : DataHolder<T>()

    data class Fail(val errorResourceId : Int?=null,
                    val errStr: String=DEFAULT_ERROR_STR,
                    val baseError: BaseError?=null
    ) : DataHolder<Nothing>()

    data class Loading(val loadingResourceId : Int?=null,
                       val loadingStr: String=DEFAULT_LOADING_STR
                       ) : DataHolder<Nothing>()

}