package com.huawei.references.hinotes.ui.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.item.model.Item
import com.huawei.references.hinotes.ui.notes.NotesFragmentDirections
import kotlinx.android.synthetic.main.note_item_list.view.*

class NotesAdapter(var notesList:ArrayList<Item>) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    class NotesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item_list,parent,false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.itemView.note_title.text = notesList[position].itemId.toString()
        holder.itemView.note_description.text = notesList[position].poiDescription
        holder.itemView.note_created_date.text = notesList[position].createdAt.toString()
        holder.itemView.setOnClickListener {
            val action = NotesFragmentDirections.actionNavigationNotesToAddNoteFragment()
            Navigation.findNavController(it).navigate(action)
        }

        if(notesList[position].isPinned){
            holder.itemView.pin_icon.alpha=1f
        }

        holder.itemView.pin_icon.setOnClickListener {
            it.alpha=1f
        }
    }

    fun updateNotesList(newNotesList:List<Item>){
        notesList.clear()
        notesList.addAll(newNotesList)
        notifyDataSetChanged()
    }

    fun updateNotesIndex(){
        notifyItemMoved(2, 0);
    }

}