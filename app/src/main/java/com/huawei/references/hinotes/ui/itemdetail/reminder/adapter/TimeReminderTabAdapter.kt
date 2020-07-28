package com.huawei.references.hinotes.ui.itemdetail.reminder.adapter

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class TimeReminderTabAdapter internal constructor(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
    private val mFragmentList: MutableList<Fragment> =
        ArrayList<Fragment>()
    private val mFragmentTitleList: MutableList<String> =
        ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}