package com.example.musicplayer.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.*
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.MainActivity
import com.example.musicplayer.R
import com.example.musicplayer.fragments.AllSongsFragment
import com.example.musicplayer.fragments.MusicControllerFragment
import java.util.*
import kotlin.coroutines.suspendCoroutine

class SongAdapter(
    private val context: Context,
    var dataSource: MutableList<GlobalClass.Music>,
    fragment: Fragment
) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val fragment = fragment
    private var listOfColors =
        arrayOf(R.color.colorCard1, R.color.colorCard2, R.color.colorCard3, R.color.colorCard4)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.sample_song_card, parent, false)
        val titleText: TextView = rowView.findViewById(R.id.songNameText)
        val imageText: TextView = rowView.findViewById(R.id.songImageView)
        val heartImage: ImageView = rowView.findViewById(R.id.songHeart)
        Log.d("favourites", "created ${position}")
        val music = getItem(position) as GlobalClass.Music
        titleText.text = music.title
        imageText.text = music.title[0].toString().toUpperCase(Locale.ROOT)
        val index = (0..3).random()
        imageText.background = context.resources.getColor(listOfColors[index]).toDrawable()
        with(titleText) {
            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
            setHorizontallyScrolling(true)
            marqueeRepeatLimit = -1
            isSelected = true
            text = music.title
        }
        titleText.setTextColor(context.resources.getColor(R.color.textWhite))
        titleText.setOnClickListener {
            val mApp = GlobalClass.objectInstance
            mApp.addToRecentlyPlayed(music)
            if (!mApp.hasStopped() && music==GlobalClass.currentPlaying) {
                Log.d("progress", "already playing")
                (fragment.activity as MainActivity).changeFragmentToMusic(MusicControllerFragment())
            } else {
                mApp.releasePlayer()
                mApp.clearPlaylist()
                setPlaylist(music)
                mApp.playMusic(music)
                (fragment.activity as MainActivity).changeFragmentToMusic(MusicControllerFragment())
            }
        }
        imageText.setOnClickListener {
            val mApp = GlobalClass.objectInstance
            mApp.addToRecentlyPlayed(music)
            if (!mApp.hasStopped() && music==GlobalClass.currentPlaying) {
                (fragment.activity as MainActivity).changeFragmentToMusic(MusicControllerFragment())
            } else {
                mApp.releasePlayer()
                mApp.clearPlaylist()
                setPlaylist(music)
                mApp.playMusic(music)
                (fragment.activity as MainActivity).changeFragmentToMusic(MusicControllerFragment())
            }
        }
        if (GlobalClass.favourites.any {
                it.title == music.title
            }) {
            val indexOfFavourite = GlobalClass.songList.indexOfFirst {
                it.title == music.title
            }
            GlobalClass.songList[indexOfFavourite].isHeartSelected=1
            heartImage.setImageResource(R.drawable.ic_untitled)
        }
        heartImage.setOnClickListener {
            if (music.isHeartSelected == 0) {
                val globalObject = GlobalClass.objectInstance
                globalObject.addToFavourites(music)
                heartImage.setImageResource(R.drawable.ic_untitled)
                music.isHeartSelected = 1
            } else {
                val globalObject = GlobalClass.objectInstance
                globalObject.removeFromFavourites(music)
                heartImage.setImageResource(R.drawable.ic_heart)
                music.isHeartSelected = 0
            }
        }
        return rowView
    }
    private fun setPlaylist(music : GlobalClass.Music) {
        GlobalScope.launch {
            val arrayStart : MutableList<GlobalClass.Music> = mutableListOf()
            val indexOfSong = dataSource.indexOf(music)
            arrayStart.addAll(dataSource.subList(indexOfSong,dataSource.size))
            arrayStart.addAll(dataSource.subList(0,indexOfSong))
            GlobalClass.currentIndex = 0
            GlobalClass.currentPlaylist.clear()
            GlobalClass.currentPlaylist = arrayStart
            for(i in arrayStart) {
                Log.d("playlists","${i.title} \n")
            }
        }
    }
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }
}