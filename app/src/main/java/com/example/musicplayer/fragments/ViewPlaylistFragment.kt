package com.example.musicplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass

import com.example.musicplayer.R
import com.example.musicplayer.adapters.FavouritesSongAdapter
import com.example.musicplayer.adapters.SongAdapter

class ViewPlaylistFragment : Fragment() {

    private lateinit var viewPlaylistRecycler : RecyclerView
    private lateinit var playlistHeading : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPlaylistRecycler = view.findViewById(R.id.viewPlaylistRecycler)
        playlistHeading = view.findViewById(R.id.textView4)
        val key = arguments?.getString("key")
        playlistHeading.text = key
        val listOfSongs = GlobalClass.playlists[key]
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter = FavouritesSongAdapter(listOfSongs!!, this)
        viewPlaylistRecycler.layoutManager = layoutManager
        viewPlaylistRecycler.adapter = adapter
    }
}
