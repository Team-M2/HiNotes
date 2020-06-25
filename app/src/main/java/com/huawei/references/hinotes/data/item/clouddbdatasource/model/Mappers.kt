package com.huawei.references.hinotes.data.item.clouddbdatasource.model

import com.huawei.references.hinotes.data.item.model.TodoListSubItem
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.Permission
import com.huawei.references.hinotes.data.item.model.UserRole

fun PermissionCDBDTO.mapToPermission() : Permission {
    return Permission(this.itemId,this.userId,UserRole.valueOf(this.role)!!)
}

fun Permission.mapToPermissionDTO() : PermissionCDBDTO {
    return PermissionCDBDTO(
        this.itemId,
        this.userId,
        this.userRole.role
    )
}

fun ItemCDBDTO.mapToItem(todoListSubItems:List<TodoListSubItem>?=null) : Item {
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

fun Item.mapToItemDTO() : ItemCDBDTO {
    return ItemCDBDTO(
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