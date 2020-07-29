package com.huawei.references.hinotes.data

import com.huawei.references.hinotes.data.item.itemDataModule
import com.huawei.references.hinotes.data.item.restdatasource.restDataModule
import com.huawei.references.hinotes.data.ml.mlDataModule

val dataModule = itemDataModule + restDataModule + mlDataModule