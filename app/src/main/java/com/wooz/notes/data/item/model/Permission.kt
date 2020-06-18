package com.wooz.notes.data.item.model

data class Permission(val itemId:Int,
                      val userId:String,
                      val userRole:UserRole)