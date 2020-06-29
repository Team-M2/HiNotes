package com.huawei.references.hinotes.data.item.restdatasource.model

import java.util.*

data class TodoListSubItemRestDTO(val id:Int?,
                                  val itemId:Int,
                                  val createdAt:Date?,
                                  val updatedAt: Date?,
                                  val title:String,
                                  val isChecked:Boolean
)

