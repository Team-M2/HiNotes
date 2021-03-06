package com.huawei.references.hinotes.ui.itemlist

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.huawei.agconnect.auth.AGConnectUser
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.base.DataHolder
import com.huawei.references.hinotes.data.base.NoRecordFoundError
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.data.item.model.ItemType
import com.huawei.references.hinotes.data.item.model.UserRole
import com.huawei.references.hinotes.ui.base.*
import com.huawei.references.hinotes.ui.itemlist.notes.adapter.IOnLongClickListener
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

abstract class ItemListBaseFragment() : BaseFragment(), IOnLongClickListener {

    protected val sectionedAdapter: SectionedRecyclerViewAdapter = SectionedRecyclerViewAdapter()
    private val userMyItemList:ArrayList<Item> = arrayListOf()
    private val userSharedItemList:ArrayList<Item> = arrayListOf()

    abstract fun getRecyclerView() : RecyclerView

    abstract val myItemsSectionAdapter: SectionAdapter
    abstract val sharedItemsSectionAdapter: SectionAdapter

    abstract val pageTitle:Int

    protected abstract fun getItemListViewModel() : ItemListViewModel

    abstract val itemType:ItemType

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeDataHolderLiveData(getItemListViewModel().deleteILiveData){
            userMyItemList.removeAll(myItemsSectionAdapter.getSelectedItems())
            userSharedItemList.removeAll(sharedItemsSectionAdapter.getSelectedItems())
            updateRV()
        }
        observeDataHolderLiveData(getItemListViewModel().itemsLiveData,{
            (requireActivity() as? BaseActivity)?.
                customToast(getString(R.string.no_recorded_note),false)
            fillLists(listOf())
        }){
            fillLists(it)
        }
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<TextView>(R.id.toolbar_title)?.text=getString(pageTitle)
        requireActivity().findViewById<ImageView>(R.id.toolbar_cancel_icon).setOnClickListener {
            setDefaultToolbar()
            myItemsSectionAdapter.clearSelections()
            sharedItemsSectionAdapter.clearSelections()
            sectionedAdapter.notifyDataSetChanged()
        }
        requireActivity().findViewById<ImageView>(R.id.toolbar_delete_icon).setOnClickListener {
            runWithAGConnectUserOrOpenLogin {
                getItemListViewModel().deleteItems(myItemsSectionAdapter.getSelectedItems() +
                        sharedItemsSectionAdapter.getSelectedItems(),it.uid)
                sectionedAdapter.notifyDataSetChanged()
            }
        }

        getRecyclerView().apply {
            layoutManager= LinearLayoutManager(context)
            adapter=sectionedAdapter
        }
        view.findViewById<SwipeRefreshLayout>(R.id.item_swipe_refresh_layout)?.apply {
            setOnRefreshListener {
                getData()
                isRefreshing=false
            }
        }
        setDefaultToolbar()
    }

    override fun setOnLongClickListener() {
        val deleteIcon: ImageView = requireActivity().findViewById(R.id.toolbar_delete_icon)
        val cancelIcon: ImageView = requireActivity().findViewById(R.id.toolbar_cancel_icon)
        val signOutIcon: ImageView = requireActivity().findViewById(R.id.toolbar_sign_out_icon)
        deleteIcon.visibility = View.VISIBLE
        cancelIcon.visibility = View.VISIBLE
        signOutIcon.visibility = View.GONE
    }

    private fun setDefaultToolbar(){
        val deleteIcon: ImageView = requireActivity().findViewById(R.id.toolbar_delete_icon)
        val cancelIcon: ImageView = requireActivity().findViewById(R.id.toolbar_cancel_icon)
        val signOutIcon: ImageView = requireActivity().findViewById(R.id.toolbar_sign_out_icon)
        deleteIcon.visibility = View.GONE
        cancelIcon.visibility = View.GONE
        signOutIcon.visibility = View.VISIBLE
    }

    private fun fillLists(items:List<Item>){
        myItemsSectionAdapter.clearSelections()
        sharedItemsSectionAdapter.clearSelections()
        userMyItemList.clear()
        userSharedItemList.clear()
        items.forEach {
            if(it.role==UserRole.Owner) userMyItemList.add(it)
            else userSharedItemList.add(it)
        }
        updateRV()
    }

    private fun updateRV(){
        sectionedAdapter.apply {
            removeAllSections()
            addSection(myItemsSectionAdapter.apply {
                setItems(userMyItemList)
            } as Section)
            addSection(sharedItemsSectionAdapter.apply {
                setItems(userSharedItemList)
            } as Section)
            notifyDataSetChanged()
        }
        setDefaultToolbar()
    }

    protected fun runWithAGConnectUserOrOpenLogin(runBlock: (agConnectUser: AGConnectUser) -> Unit){
        agConnectAuth?.currentUser?.let {
            runBlock.invoke(it)
        } ?: kotlin.run {
            (activity as? BaseActivity)?.let {
                it.openLoginActivity()
            }
        }
    }

    fun <T : Any>observeDataHolderLiveData(liveData:LiveData<DataHolder<T>>,
                                           noResultBlock: () -> Unit= {},
                                           runBlock: (data:T) -> Unit = {}){
        liveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is DataHolder.Success->{
                    view?.findViewById<ProgressBar>(R.id.item_progressbar)?.hide()
                    runBlock.invoke(it.data)
                }
                is DataHolder.Fail->{
                    view?.findViewById<ProgressBar>(R.id.item_progressbar)?.hide()
                    if(it.baseError is NoRecordFoundError){
                        noResultBlock.invoke()
                    }
                    else Toast.makeText(requireContext(),it.errStr,Toast.LENGTH_LONG).show()
                }
                is DataHolder.Loading->{
                    view?.findViewById<ProgressBar>(R.id.item_progressbar)?.show()
                }
            }
        })
    }

    abstract fun getData()

}