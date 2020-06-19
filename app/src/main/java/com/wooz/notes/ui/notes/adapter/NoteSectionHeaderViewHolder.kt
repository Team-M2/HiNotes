package com.wooz.notes.ui.notes.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wooz.notes.R

internal class NoteSectionHeaderViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    val noteSectionHeader: TextView = view.findViewById(R.id.note_section_header_title)
}