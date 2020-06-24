package com.wooz.notes.data.item.restdatasource.model

import com.google.gson.annotations.SerializedName

data class QueryRequestRestDTO (@SerializedName("query") val query: String)