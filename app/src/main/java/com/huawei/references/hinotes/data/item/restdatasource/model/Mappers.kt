package com.huawei.references.hinotes.data.item.restdatasource.model

import com.huawei.hms.maps.model.LatLng
import com.huawei.references.hinotes.data.item.model.*

fun PermissionRestDTO.mapToPermission() : Permission {
    return Permission(itemId!!,userId!!,UserRole.valueOf(role!!)!!)
}

fun Permission.mapToPermissionDTO() : PermissionRestDTO {
    return PermissionRestDTO(itemId,userId,userRole.role)
}

fun ItemRestDTO.mapToItem(todoListSubItems: List<TodoListSubItem> = mutableListOf()) : Item {
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
        UserRole.valueOf(this.role!!),
        reminder,
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
        poiName,
        poiDescription,
        title,
        isChecked,
        isPinned,
        reminder,
        role!!.role
    )
}

fun TodoListSubItemRestDTO.mapToTodoListSubItem() : TodoListSubItem{
    return TodoListSubItem(id!!,itemId,createdAt,updatedAt,title,isChecked)
}

fun ReminderRestDTO.mapToReminder() : Reminder= Reminder(this.id!!,
    this.itemId,
    this.title,
    LatLng(this.lat!!,this.lng!!),
    this.radius,
    this.date,
    ReminderType.valueOf(this.reminderType!!)!!
    )

fun Reminder.mapToReminderRestDTO() : ReminderRestDTO= ReminderRestDTO(this.id,
    this.itemId,
    this.title,
    this.location!!.latitude,
    this.location!!.longitude,
    this.radius,
    this.date,
    this.reminderType!!.type
)