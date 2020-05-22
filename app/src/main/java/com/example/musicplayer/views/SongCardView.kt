package com.example.musicplayer.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.R
import java.util.*

class SongCardView(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.sample_song_card, parent, false)) {

    var titleText: TextView? = null
    var imageText: TextView? = null
    var heartImage: ImageView? = null
    private var listOfColors =
        arrayOf(R.color.colorCard1, R.color.colorCard2, R.color.colorCard3, R.color.colorCard4)

    init {
        titleText = itemView.findViewById(R.id.songNameText)
        imageText = itemView.findViewById(R.id.songImageView)
        heartImage = itemView.findViewById(R.id.songHeart)
    }

    fun bind(music: GlobalClass.Music, context: Context) {
        titleText!!.text = music.title
        imageText!!.text = music.title[0].toString().toUpperCase(Locale.ROOT)
        val index = (0..3).random()
        imageText!!.background = context.resources.getColor(listOfColors[index]).toDrawable()
        with(titleText!!) {
            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
            setHorizontallyScrolling(true)
            marqueeRepeatLimit = -1
            isSelected = true
            text = music.title
        }
        titleText!!.setTextColor(context.resources.getColor(R.color.textWhite))
        if (GlobalClass.favourites.any {
                it.title == music.title
            }) {
            val indexOfFavourite = GlobalClass.songList.indexOfFirst {
                it.title == music.title
            }
            GlobalClass.songList[indexOfFavourite].isHeartSelected=1
            heartImage!!.setImageResource(R.drawable.ic_untitled)
        }
    }

}