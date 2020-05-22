package com.example.musicplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.MainActivity

import com.example.musicplayer.R
import com.example.musicplayer.adapters.SongAdapter
import com.example.musicplayer.adapters.favouritesRecyclerAdapter
import com.example.musicplayer.adapters.playlistRecyclerAdapterHorizontal

class HomeFragment : Fragment() {

    private lateinit var recentLayout : ListView
    private lateinit var favouritesLayout : RecyclerView
    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var playlistAddText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setCurrentFragment()
        recentLayout = view.findViewById(R.id.recentlyPlayedLayout)
        favouritesLayout = view.findViewById(R.id.favouritesRecycler)
        playlistRecyclerView = view.findViewById(R.id.playlistsRecyclerHome)
        playlistAddText = view.findViewById(R.id.playlistText)
        playlistAddText.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.frag_container, CreatePlaylistFragment())
                .addToBackStack(null)
                .commit()
        }
        favouritesLayout.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.frag_container, FavouritesFragment())
                .addToBackStack(null)
                .commit()
        }
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager1 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        playlistRecyclerView.layoutManager = linearLayoutManager1
        playlistRecyclerView.adapter = playlistRecyclerAdapterHorizontal(GlobalClass.playlists.keys.toMutableList(),this)
        favouritesLayout.layoutManager = linearLayoutManager
        favouritesLayout.adapter = favouritesRecyclerAdapter(GlobalClass.favourites)
        val tempList = GlobalClass.recentlyPlayed
        val adapter = SongAdapter(activity!!.applicationContext, tempList,this)
        recentLayout.adapter = adapter
    }
}
