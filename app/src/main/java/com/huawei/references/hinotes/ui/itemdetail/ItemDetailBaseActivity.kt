package com.huawei.references.hinotes.ui.itemdetail

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.huawei.agconnect.auth.AGConnectUser
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.ui.base.BaseActivity
import com.huawei.references.hinotes.ui.base.customToast
import com.huawei.references.hinotes.ui.base.hide
import com.huawei.references.hinotes.ui.base.show
import kotlinx.android.synthetic.main.activity_detail_todo_list.*

abstract class ItemDetailBaseActivity() : BaseActivity() {


    abstract fun getItemDetailViewModel() : ItemDetailViewModel

    override fun onStart() {
        super.onStart()
        observeDataHolderLiveData(getItemDetailViewModel().saveItemLiveData){
            customToast(this,this.getString(R.string.note_successfully_saved),false)
        }

        observeDataHolderLiveData(getItemDetailViewModel().deleteItemLiveData){
            customToast(this,this.getString(R.string.note_successfully_deleted),false)
            finish()
        }
    }

    protected fun runWithAGConnectUserOrOpenLogin(runBlock: (agConnectUser: AGConnectUser) -> Unit){
        agConnectAuth?.currentUser?.let {
            runBlock.invoke(it)
        } ?: kotlin.run {
            openLoginActivity()
        }
    }

    protected fun <T : Any>observeDataHolderLiveData(liveData: LiveData<DataHolder<T>>,
                                                     noResultBlock: () -> Unit= {},
                                           runBlock: (data:T) -> Unit){
        liveData.observe(this, Observer {
            when(it){
                is DataHolder.Success->{
                    detail_progress_bar.hide()
                    runBlock.invoke(it.data)
                }
                is DataHolder.Fail->{
                    detail_progress_bar.hide()
                    customToast(this,this.getString(R.string.failed_get_data),true)
                    if(it.baseError is NoRecordFoundError){
                        noResultBlock.invoke()
                    }
                    else Toast.makeText(this,it.errStr, Toast.LENGTH_LONG).show()
                }
                is DataHolder.Loading->{
                    detail_progress_bar.show()
                }
            }
        })
    }
}