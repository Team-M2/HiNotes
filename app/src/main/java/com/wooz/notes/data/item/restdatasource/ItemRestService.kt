package com.wooz.notes.data.item.restdatasource

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ItemRestService {
    @POST("makeQuery")
    suspend fun executeQuery(@Body queryString: String) : Response<String>
}