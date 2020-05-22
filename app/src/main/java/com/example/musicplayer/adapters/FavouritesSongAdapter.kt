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
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.MainActivity
import com.example.musicplayer.R
import com.example.musicplayer.fragments.AllSongsFragment
import com.example.musicplayer.fragments.MusicControllerFragment
import com.example.musicplayer.views.SongCardView
import com.example.musicplayer.views.SongNameView
import java.util.*
import kotlin.coroutines.suspendCoroutine

class FavouritesSongAdapter(
    private val list: MutableList<GlobalClass.Music>,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<SongCardView>() {

    var playlist: MutableList<GlobalClass.Music> = mutableListOf()
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongCardView {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return SongCardView(inflater, parent)
    }

    override fun onBindViewHolder(holder: SongCardView, position: Int) {
        val music: GlobalClass.Music = list[position]
        holder.bind(music, context)
        holder.titleText!!.setOnClickListener {
            val mApp = GlobalClass.objectInstance
            mApp.addToRecentlyPlayed(music)
            if (!mApp.hasStopped() && music == GlobalClass.currentPlaying) {
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
        holder.imageText!!.setOnClickListener {
            val mApp = GlobalClass.objectInstance
            mApp.addToRecentlyPlayed(music)
            if (!mApp.hasStopped() && music == GlobalClass.currentPlaying) {
                (fragment.activity as MainActivity).changeFragmentToMusic(MusicControllerFragment())
            } else {
                mApp.releasePlayer()
                mApp.clearPlaylist()
                setPlaylist(music)
                mApp.playMusic(music)
                (fragment.activity as MainActivity).changeFragmentToMusic(MusicControllerFragment())
            }
        }
        holder.heartImage!!.setOnClickListener {
            if (music.isHeartSelected == 0) {
                val globalObject = GlobalClass.objectInstance
                globalObject.addToFavourites(music)
                holder.heartImage!!.setImageResource(R.drawable.ic_untitled)
                music.isHeartSelected = 1
            } else {
                val globalObject = GlobalClass.objectInstance
                globalObject.removeFromFavourites(music)
                holder.heartImage!!.setImageResource(R.drawable.ic_heart)
                music.isHeartSelected = 0
            }
        }
    }

    private fun setPlaylist(music: GlobalClass.Music) {
        GlobalScope.launch {
            val arrayStart: MutableList<GlobalClass.Music> = mutableListOf()
            val indexOfSong = list.indexOf(music)
            arrayStart.addAll(list.subList(indexOfSong, list.size))
            arrayStart.addAll(list.subList(0, indexOfSong))
            GlobalClass.currentIndex = 0
            GlobalClass.currentPlaylist.clear()
            GlobalClass.currentPlaylist = arrayStart
            for (i in arrayStart) {
                Log.d("playlists", "${i.title} \n")
            }
        }
    }

    override fun getItemCount(): Int = list.size

}