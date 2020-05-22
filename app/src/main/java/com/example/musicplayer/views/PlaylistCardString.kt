package com.example.musicplayer.views

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.R
import java.util.*

class PlaylistCardString(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.sample_playlist_card, parent, false)) {
    private var mTitleImage: TextView? = null
    private var mTitle: TextView? = null

    init {
        mTitleImage = itemView.findViewById(R.id.playlistCardImage)
        mTitle = itemView.findViewById(R.id.playlistCardTitle)
    }

    fun bind(music: String) {
        mTitleImage?.text = music[0].toString().toUpperCase(Locale.ROOT)
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