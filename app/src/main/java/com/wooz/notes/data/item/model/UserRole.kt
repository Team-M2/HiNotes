package com.wooz.notes.data.item.model

enum class UserRole(val role:Int) {
    Owner(0),
    Read(1),
    Write(2);

    companion object {
        fun valueOf(value: Int) = UserRole.values().find { it.role == value }
    }
}