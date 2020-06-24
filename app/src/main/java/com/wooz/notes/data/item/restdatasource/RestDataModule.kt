package com.wooz.notes.data.item.restdatasource

import com.huawei.references.hinotes.data.item.abstractions.ItemsLiveDataSource
import com.wooz.notes.data.item.abstractions.DeleteItemDataSource
import com.wooz.notes.data.item.abstractions.GetItemDataSource
import com.wooz.notes.data.item.abstractions.PermissionsDataSource
import com.wooz.notes.data.item.abstractions.UpsertItemDataSource
import com.wooz.notes.data.item.restdatasource.RestDataSourceConstants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

val restDataModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    factory {
        Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            //.addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(get())
            .build()
    }

    factory {
        val retrofit: Retrofit = get()
        retrofit.create(ItemRestService::class.java)
    }

    factory {
        ApiCallAdapter()
    }

    factory {
        GetItemDataSourceRestImpl(get(),get()) as GetItemDataSource
    }

    factory {
        PermissionsDataSourceRestImpl(get(),get()) as PermissionsDataSource
    }

    factory {
        UpsertItemDataSourceRestImpl(get(),get(),get()) as UpsertItemDataSource
    }

    factory {
        DeleteItemDataSourceRestImpl(get(),get(),get()) as DeleteItemDataSource
    }

    factory {
        ItemsLiveDataSourceRestImpl() as ItemsLiveDataSource
    }


}