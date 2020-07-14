package com.huawei.references.hinotes.data.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hms.site.api.model.Site
import com.huawei.references.hinotes.R

class SiteAdapter(private val siteList: MutableList<Site>) : RecyclerView.Adapter<SiteAdapter.ModelViewHolder>() {

    class ModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val siteName: TextView = view.findViewById(R.id.txt_site_name)
        private val siteDistance: TextView = view.findViewById(R.id.txt_site_distance)

        fun bindItems(item: Site) {
            siteName.text = item.name
            siteDistance.text = item.distance.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note_detail_site, parent, false)
        return ModelViewHolder(view)
    }

    override fun getItemCount(): Int {
        return siteList.size
    }

    override fun onBindViewHolder(holder: SiteAdapter.ModelViewHolder, position: Int) {
        holder.bindItems(siteList[position])
    }
}
