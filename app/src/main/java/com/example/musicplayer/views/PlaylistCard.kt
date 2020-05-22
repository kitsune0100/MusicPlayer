package com.example.musicplayer.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.R
import java.util.*


class PlaylistCard(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.sample_playlist_card, parent, false)) {
    private var mTitleImage: TextView? = null
    private var mTitle: TextView? = null

    init {
        mTitleImage = itemView.findViewById(R.id.playlistCardImage)
        mTitle = itemView.findViewById(R.id.playlistCardTitle)
    }

    fun bind(music: GlobalClass.Music) {
        mTitleImage?.text = music.title[0].toString().toUpperCase(Locale.ROOT)
        mTitle?.text = music.title
        with(mTitle!!) {
            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
            setHorizontallyScrolling(true)
            marqueeRepeatLimit = -1
            isSelected = true
            text = music.title
        }
    }

}
