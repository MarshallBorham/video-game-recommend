package com.example.videogame.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.videogame.R
import com.example.videogame.data.local.RecommendationEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(private var items: List<RecommendationEntity>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cover: ImageView = view.findViewById(R.id.historyCover)
        val name: TextView = view.findViewById(R.id.historyName)
        val date: TextView = view.findViewById(R.id.historyDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.gameName
        holder.date.text = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            .format(Date(item.timestamp))
        item.coverUrl?.let { url ->
            holder.cover.load("https:${url.replace("t_thumb", "t_cover_big")}")
        }
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<RecommendationEntity>) {
        items = newItems
        notifyDataSetChanged()
    }
}