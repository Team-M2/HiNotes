package com.huawei.references.hinotes.ui.itemdetail.reminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hms.site.api.model.Site
import com.huawei.references.hinotes.R
import com.huawei.references.hinotes.ui.itemlist.notes.adapter.IOnLongClickListener
import kotlinx.android.synthetic.main.nearby_poi_item_list.view.*

class PoiItemsAdapter(private var poiItemsList: MutableList<Site>,private var onPoiClickListener: IPoiClickListener) : RecyclerView.Adapter<PoiItemsAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.
        context).inflate(R.layout.nearby_poi_item_list,parent,false)

        return FeedViewHolder(view)
    }
    override fun getItemCount(): Int {
        return poiItemsList.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.itemView.poi_title.text = poiItemsList[position].name
        holder.itemView.poi_detail_address.text = poiItemsList[position].formatAddress

        holder.itemView.setOnClickListener {
            onPoiClickListener.setOnPoiClickListener(poiItemsList[position])
        }
    }

    class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}