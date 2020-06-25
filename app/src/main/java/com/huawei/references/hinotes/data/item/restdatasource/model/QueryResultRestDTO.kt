package com.huawei.references.hinotes.data.item.restdatasource.model

import com.google.gson.annotations.SerializedName

data class QueryResultRestDTO<T> (@SerializedName("status") val status: String?,
                                  @SerializedName("queryResultData") val queryResultData: List<T>?,
                                  @SerializedName("errorString") val errorString: String?
)