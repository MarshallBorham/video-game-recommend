package com.example.videogame.ui.genre

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.videogame.R

class GenreAdapter(
    private val genres: List<String>,
    private val onSelectionChanged: (Set<String>) -> Unit
) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    private val selected = mutableSetOf<String>()

    inner class ViewHolder(val checkBox: CheckBox) : RecyclerView.ViewHolder(checkBox)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cb = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_genre, parent, false) as CheckBox
        return ViewHolder(cb)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genres[position]
        holder.checkBox.text = genre
        holder.checkBox.isChecked = genre in selected
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selected.add(genre) else selected.remove(genre)
            onSelectionChanged(selected.toSet())
        }
    }

    override fun getItemCount() = genres.size
}