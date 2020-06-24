package com.wooz.notes.data.item.restdatasource.model

import com.huawei.references.hinotes.data.item.model.TodoListSubItem
import com.wooz.notes.data.item.model.Item
import com.wooz.notes.data.item.model.ItemType
import com.wooz.notes.data.item.model.Permission
import com.wooz.notes.data.item.model.UserRole

fun PermissionRestDTO.mapToPermission() : Permission {
    return Permission(this.itemId,this.userId,UserRole.valueOf(this.role)!!)
}

fun Permission.mapToPermissionDTO() : PermissionRestDTO {
    return PermissionRestDTO(this.itemId,this.userId,this.userRole.role)
}

fun ItemRestDTO.mapToItem(todoListSubItems: List<TodoListSubItem>?=null) : Item {
    return Item(
        itemId,
        createdAt,
        updatedAt,
        ItemType.valueOf(type)!!,
        isOpen,
        lat,
        lng,
        poiDescription,
        title,
        todoListSubItems,
        isChecked,
        isPinned
    )
}

fun Item.mapToItemDTO() : ItemRestDTO {
    return ItemRestDTO(
        itemId,
        createdAt,
        updatedAt,
        type.type,
        isOpen,
        lat,
        lng,
        poiDescription,
        title,
        isChecked,
        isPinned
    )
}