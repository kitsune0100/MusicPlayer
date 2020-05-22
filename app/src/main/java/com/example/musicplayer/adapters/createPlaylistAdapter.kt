package com.example.musicplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.views.PlaylistCard
import com.example.musicplayer.views.SongNameView

class createPlaylistAdapter(private val list: MutableList<GlobalClass.Music>) :
    RecyclerView.Adapter<SongNameView>() {

    var playlist: MutableList<GlobalClass.Music> = mutableListOf()
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongNameView {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return SongNameView(inflater, parent)
    }

    override fun onBindViewHolder(holder: SongNameView, position: Int) {
        val music: GlobalClass.Music = list[position]
        holder.bind(music)
        if(playlist.contains(music)) {
            holder.setBackgroundGreen(context)
        }
        holder.itemView.setOnClickListener {
            if (playlist.contains(list[position])) {
                holder.setBackgroundTransparent(context)
                playlist.remove(list[position])
            } else {
                holder.setBackgroundGreen(context)
                playlist.add(list[position])
            }
        }
    }

    override fun onViewRecycled(holder: SongNameView) {
        super.onViewRecycled(holder)
        holder.setBackgroundTransparent(context)
    }

    override fun getItemCount(): Int = list.size

    fun getList(): MutableList<GlobalClass.Music> = playlist
}