package com.huawei.references.hinotes.adapter

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.data.note.model.Item
import kotlinx.android.synthetic.main.note_item_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class NotesAdapter(var notesList:ArrayList<Item>) :
    RecyclerView.Adapter<NotesAdapter.FeedViewHolder>() {

    class FeedViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item_list,parent,false)
        return FeedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.itemView.note_title.text = notesList[position].itemId
        holder.itemView.note_description.text = notesList[position].poiDescription
        holder.itemView.note_created_date.text = notesList[position].createdAt.toString()
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