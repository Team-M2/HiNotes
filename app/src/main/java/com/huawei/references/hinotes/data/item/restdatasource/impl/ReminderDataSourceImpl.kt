package com.huawei.references.hinotes.data.item.restdatasource.impl

import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.abstractions.ReminderDataSource
import com.huawei.references.hinotes.data.item.model.Reminder
import com.huawei.references.hinotes.data.item.restdatasource.model.ReminderRestDTO
import com.huawei.references.hinotes.data.item.restdatasource.model.mapToReminder
import com.huawei.references.hinotes.data.item.restdatasource.service.ApiCallAdapter
import com.huawei.references.hinotes.data.item.restdatasource.service.DBResult
import com.huawei.references.hinotes.data.item.restdatasource.service.ItemRestService

class ReminderDataSourceImpl(private val apiCallAdapter: ApiCallAdapter,
                             private val itemRestService: ItemRestService
) : ReminderDataSource {

    override suspend fun getRemindersByItemId(itemId: Int): DataHolder<List<Reminder>> =
        apiCallAdapter.adapt<ReminderRestDTO> {
            val query= "select json_agg(json_build_object('id',\"id\",'itemId',\"itemId\",'title',\"title\",'lat',\"lat\",'lng',\"lng\",'radius',\"radius\",'date',\"date\",'reminderType',\"reminderType\")) from hinotesschema.reminder WHERE \"itemId\"=$itemId"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.ResultList -> DataHolder.Success(it.data.map { it.mapToReminder() })
                is DBResult.DBError -> DataHolder.Fail(errStr = it.errorString)
                is DBResult.EmptyQueryResult -> DataHolder.Fail(baseError = NoRecordFoundError())
                else -> DataHolder.Fail(baseError = NoRecordFoundError())
            }
        }

    override suspend fun upsert(
        reminder: Reminder,
        itemId: Int,
        isNew: Boolean
    ) : DataHolder<Int> =
        apiCallAdapter.adapt<ReminderRestDTO> {
            val query=if(isNew) generateInsertQuery(reminder, itemId)
                    else generateUpdateQuery(reminder,itemId)
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.InsertResultId -> DataHolder.Success(it.id)
                is DBResult.DBError -> DataHolder.Fail(errStr = it.errorString)
                is DBResult.EmptyQueryResult -> DataHolder.Fail(baseError = NoRecordFoundError())
                else -> DataHolder.Fail(baseError = NoRecordFoundError())
            }
        }

    private fun generateInsertQuery(reminder: Reminder,
                                    itemId: Int) =
        "insert into hinotesschema.reminder(\"itemId\",title${reminder.location?.let {",lat,lng" } ?: ""}${reminder.radius?.let { ",radius" }?:""}${reminder.date?.let { ",date" }},\"reminderType\")" +
                " values ($itemId,'${reminder.title}'${reminder.location?.let{ ",${it.latitude},${it.longitude}" } ?: ""}${reminder.radius?.let {",$it" }?:""}${reminder.date?.let {",$it" }?:""},${reminder.reminderType.type}) returning \"id\""


    private fun generateUpdateQuery(reminder: Reminder,
                                    itemId: Int) = "UPDATE hinotesschema.reminder SET \"itemId\"=$itemId,\"title\"='${reminder.title}'${reminder.location?.let{ ",lat=${it.latitude},lng=${it.longitude}" } ?: ""}${reminder.radius?.let {",radius=$it" }?:""}${reminder.date?.let {",date=$it" }?:""},\"reminderType\"=${reminder.reminderType.type} WHERE id = ${reminder.id}"

    override suspend fun deleteReminder(reminderId: Int): DataHolder<Any> {
        return apiCallAdapter.adapt<ReminderRestDTO> {
            val query="delete from hinotesschema.reminder where \"id\"=$reminderId"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.EmptyQueryResult -> DataHolder.Fail(baseError = NoRecordFoundError())
                else -> DataHolder.Fail(baseError = NoRecordFoundError())
            }
        }
    }

    override suspend fun insertMultiple(reminderList: List<Reminder>, itemId: Int): DataHolder<Any> {
        return apiCallAdapter.adapt<Any> {
            val query="BEGIN; ${reminderList.joinToString(";"){ generateInsertQuery(it,itemId)}}; COMMIT;"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.EmptyQueryResult -> DataHolder.Success(Any())
                else -> DataHolder.Fail()
            }
        }
    }

    override suspend fun deleteReminders(reminderIds: List<Int>): DataHolder<Any> {
        return apiCallAdapter.adapt<ReminderRestDTO> {
            val query="BEGIN; ${reminderIds.joinToString(";") { "delete from hinotesschema.reminder where \"id\"=$it"}}; COMMIT;"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.EmptyQueryResult -> DataHolder.Success(Any())
                else -> DataHolder.Fail()
            }
        }
    }

    override suspend fun updateMultiple(reminderList: List<Reminder>, itemId: Int): DataHolder<Any> {
        return apiCallAdapter.adapt<ReminderRestDTO> {
            val query="BEGIN; ${reminderList.joinToString(";") {generateUpdateQuery(it,itemId)}}; COMMIT;"
            itemRestService.executeQuery(query)
        }.let {
            when(it){
                is DBResult.EmptyQueryResult -> DataHolder.Success(Any())
                else -> DataHolder.Fail()
            }
        }
    }


}