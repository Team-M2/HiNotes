package com.huawei.references.hinotes.data.item.clouddbdatasource.model

import com.huawei.references.hinotes.data.item.model.*

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

fun ItemCDBDTO.mapToItem(todoListSubItems:List<TodoListSubItem> = mutableListOf(), userRole: UserRole=UserRole.Owner) : Item {
    return Item(
        itemId,
        createdAt,
        updatedAt,
        ItemType.valueOf(type)!!,
        isOpen,
        lat,
        lng,
        poiName,
        poiDescription,
        title,
        todoListSubItems.toMutableList(),
        isChecked,
        userRole,
        reminder,
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
        poiName,
        poiDescription,
        title,
        isChecked,
        reminder,
        isPinned
    )
}