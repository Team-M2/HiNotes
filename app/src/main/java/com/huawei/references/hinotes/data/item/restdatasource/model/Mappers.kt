package com.huawei.references.hinotes.data.item.restdatasource.model

import com.huawei.references.hinotes.data.item.model.*

fun PermissionRestDTO.mapToPermission() : Permission {
    return Permission(itemId!!,userId!!,UserRole.valueOf(role!!)!!)
}

fun Permission.mapToPermissionDTO() : PermissionRestDTO {
    return PermissionRestDTO(itemId,userId,userRole.role)
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

fun TodoListSubItemRestDTO.mapToTodoListSubItem() : TodoListSubItem{
    return TodoListSubItem(id!!,itemId,createdAt,updatedAt,title,isChecked)
}