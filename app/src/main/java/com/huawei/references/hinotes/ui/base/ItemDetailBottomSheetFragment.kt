package com.huawei.references.hinotes.ui.base

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.ui.itemdetail.ItemDetailBaseActivity

abstract class ItemDetailBottomSheetFragment : BottomSheetDialogFragment() {

    abstract val itemDetailBottomSheetType:ItemDetailBottomSheetType

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.delete_text)?.setOnClickListener {
            (activity as? ItemDetailBaseActivity)?.bottomSheetDeleteButtonClicked(itemDetailBottomSheetType)
            dismiss()
        }
    }

}

enum class ItemDetailBottomSheetType{
    LOCATION,REMINDER
}