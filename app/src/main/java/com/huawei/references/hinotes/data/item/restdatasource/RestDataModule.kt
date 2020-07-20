package com.huawei.references.hinotes.data.item.restdatasource

import com.huawei.references.hinotes.data.item.abstractions.*
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.restdatasource.RestDataSourceConstants.Companion.BASE_URL
import com.huawei.references.hinotes.data.item.restdatasource.impl.*
import com.huawei.references.hinotes.data.item.restdatasource.service.ApiCallAdapter
import com.huawei.references.hinotes.data.item.restdatasource.service.ItemRestService
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
        PermissionsDataSourceRestImpl(
            get(),
            get()
        ) as PermissionsDataSource
    }

    factory {
        ItemDataSourceRestImpl(get(),get(),get(),get(),get()) as ItemDataSource
    }

    factory {
        SubItemDataSourceImpl(get(),get()) as SubItemDataSource
    }

    factory {
        ReminderDataSourceImpl(get(),get()) as ReminderDataSource
    }

    single {
        LiveDataSourceRestImpl() as LiveDataSource<Item>
    }

    factory {
        SubItemLiveDataSourceRestImpl()
    }



}