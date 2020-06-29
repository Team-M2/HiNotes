package com.huawei.references.hinotes.data.item.restdatasource.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ItemRestService {
    @POST("makeQuery")
    suspend fun executeQuery(@Body queryString: String) : Response<String>
}