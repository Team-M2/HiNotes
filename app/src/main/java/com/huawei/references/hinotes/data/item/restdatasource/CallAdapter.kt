package com.wooz.notes.data.item.restdatasource

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wooz.notes.data.DataConstants.Companion.DEFAULT_SERVER_ERROR_STR
import com.wooz.notes.data.base.DataHolder
import retrofit2.Response
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ApiCallAdapter {
    inline fun <reified T : Any> adapt(serviceBody: () -> Response<String>): DataHolder<List<T>> {
        return try {
            serviceBody.invoke().let {response->
                if(response.isSuccessful){
                    response.body()?.let {
                        when {
                            it.startsWith(RestDataSourceConstants.DB_ERROR_STR) -> {
                                DataHolder.Fail(errStr = it)
                            }
                            it == RestDataSourceConstants.DB_EMPTY_LIST_STR -> {
                                DataHolder.Success(listOf<T>())
                            }
                            it.startsWith(RestDataSourceConstants.DB_NO_RESULTS_START_STR) -> {
                                DataHolder.Success(listOf<T>())
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
                                val cards: List<T>? =
                                    adapter.fromJson(it)
                                DataHolder.Success(cards!!)
                            }
                        }
                    } ?: DataHolder.Fail(errStr = DEFAULT_SERVER_ERROR_STR) 
                } else{
                    try {
                        val apiResponseError=response.errorBody()?.toString()?: DEFAULT_SERVER_ERROR_STR
                        DataHolder.Fail(errStr = apiResponseError)
                    }catch (e:Exception){
                        DataHolder.Fail(errStr = DEFAULT_SERVER_ERROR_STR)
                    }
                }
            }
        }
        catch (e:Exception){
            DataHolder.Fail(errStr = DEFAULT_SERVER_ERROR_STR) 
        }
    }
}