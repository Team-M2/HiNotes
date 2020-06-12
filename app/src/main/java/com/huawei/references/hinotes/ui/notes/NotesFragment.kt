package com.huawei.references.hinotes.ui.notes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.base.BaseFragment
import com.huawei.references.hinotes.data.base.DataHolder
import kotlinx.android.synthetic.main.fragment_notes.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotesFragment : BaseFragment() {

    private val notesViewModel: NotesViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        notesViewModel.text.observe(viewLifecycleOwner, Observer {
            textNotes.text = it
        })

        notesViewModel.itemsLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is DataHolder.Success ->{
                    // populate list with data, hide loading indicator
                    it.data.forEach {
                        Toast.makeText(requireContext(),
                            it.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }

                is DataHolder.Fail ->{
                    // show error indicator
                }

                is DataHolder.Loading ->{
                    // show loading indicator
                }
            }
        })

        getNotesButton.setOnClickListener {
            notesViewModel.getNotes("")
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notes, container, false)


}