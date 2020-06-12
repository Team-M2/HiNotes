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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.adapter.NotesAdapter
import com.huawei.references.hinotes.base.BaseFragment
import com.huawei.references.hinotes.data.base.DataHolder
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class NotesFragment : BaseFragment() {

    private val notesViewModel: NotesViewModel by viewModel()
    private val notesAdapter = NotesAdapter(arrayListOf())

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notesViewModel.getNotes(1)
        notes_recycler_view.layoutManager=LinearLayoutManager(context)
        notes_recycler_view.adapter=notesAdapter

        notesViewModel.text.observe(viewLifecycleOwner, Observer {
            textNotes.text = it
        })

        notesViewModel.itemsLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is DataHolder.Success ->{
                    // populate list with data, hide loading indicator
                    it.data.forEach {
                    /*    Toast.makeText(requireContext(),
                            it.toString(),
                            Toast.LENGTH_SHORT).show()
                     */
                }
                    notesAdapter.updateNotesList(it.data)

                    /*
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3000)
                        notesAdapter.updateNotesIndex()
                    }
                     */
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
            val action = NotesFragmentDirections.actionNavigationNotesToAddNoteFragment()
            Navigation.findNavController(it).navigate(action)

            //   notesViewModel.getNotes(1)
         //   notes_recycler_view.layoutManager=LinearLayoutManager(context)
         //   notes_recycler_view.adapter=notesAdapter

            // first swap the item using  Collections.swap() method

        }

        floatingActionButton.setOnClickListener {
            val action = NotesFragmentDirections.actionNavigationNotesToAddNoteFragment()
            Navigation.findNavController(it).navigate(action)
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notes, container, false)
}