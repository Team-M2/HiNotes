package com.huawei.references.hinotes.data.item.model

fun PermissionDTO.mapToPermission() : Permission{
    return Permission(this.itemId,this.userId,UserRole.valueOf(this.role)!!)
}

fun Permission.mapToPermissionDTO() : PermissionDTO{
    return PermissionDTO(this.itemId,this.userId,this.userRole.role)
}

fun ItemDTO.mapToItem() : Item{
    return Item(itemId,
        createdAt,
        updatedAt,
        ItemType.valueOf(type)!!,
        isOpen,
        lat,
        lng,
        poiDescription,
        UserRole.valueOf(role)!!
    )
}

fun Item.mapToItemDTO() : ItemDTO{
    return ItemDTO(itemId,
        createdAt,
        updatedAt,
        type.type,
        isOpen,
        lat,
        lng,
        poiDescription,
        role.role
    )
}