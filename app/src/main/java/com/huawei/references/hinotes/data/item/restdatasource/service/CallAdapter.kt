package com.huawei.references.hinotes.data.item.restdatasource.service

import com.huawei.references.hinotes.data.DataConstants.Companion.DEFAULT_SERVER_ERROR_STR
import com.huawei.references.hinotes.data.item.restdatasource.RestDataSourceConstants
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ApiCallAdapter {
    inline fun <reified T : Any> adapt(serviceBody: () -> Response<String>): DBResult<T> {
        return try {
            serviceBody.invoke().let {response->
                if(response.isSuccessful){
                    response.body()?.let {
                        when {
                            it.toLowerCase().startsWith(RestDataSourceConstants.DB_ERROR_STR) -> {
                                DBResult.EmptyQueryResult()
                            }
                            it == RestDataSourceConstants.DB_EMPTY_LIST_STR -> {
                                DBResult.EmptyQueryResult()
                            }
                            it.startsWith(RestDataSourceConstants.DB_NO_RESULTS_START_STR) -> {
                                DBResult.EmptyQueryResult()
                            }
                            it.startsWith(RestDataSourceConstants.DB_DELETE_STR) -> {
                                DBResult.SuccessfulOperation()
                            }
//                            it.startsWith(RestDataSourceConstants.DB_NO_RESULTS_RETURNING_STR) -> {
//                                DBResult.SuccessfulOperation()
//                            }
                            it.startsWith(RestDataSourceConstants.DB_UPDATE_STR) -> {
                                DBResult.SuccessfulOperation()
                            }
                            it.startsWith(RestDataSourceConstants.DB_COMMIT_STR) -> {
                                DBResult.SuccessfulOperation()
                            }
                            it.toIntOrNull() !=null ->{
                                // a row inserted
                                DBResult.InsertResultId(it.toInt())
                            }
                            else -> {
                                val type: Type = Types.newParameterizedType(
                                    MutableList::class.java,
                                    T::class.java
                                )
                                val adapter: JsonAdapter<List<T>> =
                                    Moshi.Builder()
                                        .add(KotlinJsonAdapterFactory())
                                        .add(object : Any(){
                                            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                            @ToJson
                                            @Synchronized
                                            fun dateToJson(d: Date?): String? {
                                                return dateFormat.format(d)
                                            }
                                            @FromJson
                                            @Synchronized
                                            @Throws(ParseException::class)
                                            fun dateToJson(s: String?): Date? {
                                                return dateFormat.parse(s)
                                            }
                                        })
                                        .build().adapter(type)
                                val elements: List<T>? = adapter.fromJson(it)
                                DBResult.ResultList(elements!!)
                            }
                        }
                    } ?: DBResult.DBError(DEFAULT_SERVER_ERROR_STR)
                } else{
                    try {
                        val apiResponseError=response.errorBody()?.toString()?: DEFAULT_SERVER_ERROR_STR
                        DBResult.DBError(apiResponseError)
                    }catch (e:Exception){
                        DBResult.DBError(DEFAULT_SERVER_ERROR_STR)
                    }
                }
            }
        }
        catch (e:Exception){
            DBResult.DBError(DEFAULT_SERVER_ERROR_STR)
        }
    }
}