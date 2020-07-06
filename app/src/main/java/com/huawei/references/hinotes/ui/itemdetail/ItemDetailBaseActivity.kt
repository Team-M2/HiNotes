package com.huawei.references.hinotes.ui.itemdetail

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.huawei.agconnect.auth.AGConnectUser
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.ui.base.BaseActivity

abstract class ItemDetailBaseActivity() : BaseActivity() {


    abstract fun getItemDetailViewModel() : ItemDetailViewModel

    override fun onStart() {
        super.onStart()
        observeDataHolderLiveData(getItemDetailViewModel().saveItemLiveData){
            //TODO: show save successful popup and finish with activity result. Refresh in returned activity
        }

        observeDataHolderLiveData(getItemDetailViewModel().deleteItemLiveData){
            //TODO: show delete successfull popup and finish with activity result. Refresh in returned activity
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
                    //TODO: hide loading popup
                    runBlock.invoke(it.data)
                }
                is DataHolder.Fail->{
                    //TODO: hide loading popup
                    //TODO: show error popup with message
                    if(it.baseError is NoRecordFoundError){
                        noResultBlock.invoke()
                    }
                    else Toast.makeText(this,it.errStr, Toast.LENGTH_LONG).show()
                }
                is DataHolder.Loading->{
                    //TODO: show loading popup
                }
            }
        })
    }

}