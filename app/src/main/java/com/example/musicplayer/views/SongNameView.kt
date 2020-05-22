package com.example.musicplayer.views

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.R
import java.util.*

class SongNameView(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.songnamelayout, parent, false)) {
    var mTitle: TextView? = null

    init {
        mTitle = itemView.findViewById(R.id.songNameLayoutText)
    }

    fun bind(music: GlobalClass.Music) {
        mTitle?.text = music.title
        with(mTitle!!) {
            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
            setHorizontallyScrolling(true)
            marqueeRepeatLimit = -1
            isSelected = true
            text = music.title
        }
    }

    fun setBackgroundGreen(context: Context) {
        mTitle?.background = context.resources.getDrawable(R.color.textGreen)
    }

    fun setBackgroundTransparent(context: Context) {
        mTitle?.background = context.resources.getDrawable(android.R.color.transparent)
    }

}