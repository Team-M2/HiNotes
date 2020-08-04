/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.references.hinotes.data.item.clouddbdatasource

import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.ObjectTypeInfo
import com.huawei.references.hinotes.data.item.clouddbdatasource.model.PermissionCDBDTO

/**
 * Definition of ObjectType Helper.
 *
 * @since 2019-12-06
 */
object ObjectTypeInfoHelper {
    private const val FORMAT_VERSION = 1
    private const val OBJECT_TYPE_VERSION = 2

    val objectTypeInfo= ObjectTypeInfo().apply{
            formatVersion = FORMAT_VERSION
            objectTypeVersion = OBJECT_TYPE_VERSION.toLong()
            objectTypes= mutableListOf(
                PermissionCDBDTO::class.java
                //,ItemDTO::class.java
            ).map{
                it as Class<out CloudDBZoneObject>
            }
        }
}

