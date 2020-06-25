package com.huawei.references.hinotes.data.item.restdatasource.model;

import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.annotations.PrimaryKey;

public class PermissionRestDTO extends CloudDBZoneObject{
    @PrimaryKey
    public Integer itemId;
    @PrimaryKey
    public String userId;
    @PrimaryKey
    public Integer role;

    public PermissionRestDTO(){
        super();
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public PermissionRestDTO(int itemId, String userId, int role) {
        this.itemId = itemId;
        this.userId = userId;
        this.role = role;
    }
}


