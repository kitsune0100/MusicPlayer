package com.example.musicplayer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.MainActivity

import com.example.musicplayer.R
import com.example.musicplayer.adapters.ViewAllPlaylistsAdapter

class ViewAllPlaylistsFragments : Fragment() {

    private lateinit var allPlaylistsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_all_playlists_fragments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setCurrentFragment()
        allPlaylistsRecyclerView = view.findViewById(R.id.allPlaylistsRecycler)
        Log.d("playlists", GlobalClass.playlists.keys.toString())
        val layoutManagerPlaylists = LinearLayoutManager(activity!!.applicationContext, LinearLayoutManager.VERTICAL, false)
        val adapter = ViewAllPlaylistsAdapter(GlobalClass.playlists.keys.toMutableList(),this)
        allPlaylistsRecyclerView.layoutManager = layoutManagerPlaylists
        allPlaylistsRecyclerView.adapter = adapter
    }

}
