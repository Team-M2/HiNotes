package com.huawei.references.hinotes.data.item.restdatasource.service

sealed class DBResult<out T: Any> {

    data class ResultList<out T : Any>(val data:List<T>) : DBResult<T>()

    class EmptyQueryResult() : DBResult<Nothing>()

    class DBError(val errorString:String) : DBResult<Nothing>()

    data class InsertResultId(val id:Int) : DBResult<Nothing>()

    class SuccessfulOperation() : DBResult<Nothing>()

}