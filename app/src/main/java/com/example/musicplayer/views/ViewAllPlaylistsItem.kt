package com.example.musicplayer.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.R

class ViewAllPlaylistsItem(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.view_all_playlists_item_view,
            parent,
            false
        )
    ) {

    private var mTitle: TextView? = null

    init {
        mTitle = itemView.findViewById(R.id.allPlaylistsCardText)
    }

    fun bind(music: String) {
        mTitle?.text = music
        with(mTitle!!) {
            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
            setHorizontallyScrolling(true)
            marqueeRepeatLimit = -1
            isSelected = true
            text = music
        }
    }

}