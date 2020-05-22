package com.example.musicplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.views.PlaylistCard

class favouritesRecyclerAdapter(private val list : MutableList<GlobalClass.Music>) : RecyclerView.Adapter<PlaylistCard>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistCard {
        val inflater = LayoutInflater.from(parent.context)
        return PlaylistCard(inflater, parent)
    }

    override fun onBindViewHolder(holder: PlaylistCard, position: Int) {
        val music : GlobalClass.Music = list[position]
        holder.bind(music)
    }

    override fun getItemCount(): Int = list.size

}