package com.huawei.references.hinotes.ui.base

import androidx.fragment.app.Fragment
import com.huawei.agconnect.auth.AGConnectAuth

open class BaseFragment: Fragment(){

    protected val agConnectAuth = AGConnectAuth.getInstance()


}