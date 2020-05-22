package com.example.musicplayer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.MainActivity

import com.example.musicplayer.R
import com.example.musicplayer.adapters.FavouritesSongAdapter
import com.example.musicplayer.adapters.SongAdapter

class FavouritesFragment : Fragment() {
    private lateinit var favouritesListLayout : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setCurrentFragment()
        favouritesListLayout = view.findViewById(R.id.favouritesListLayout)
        for (i in GlobalClass.favourites) {
            Log.d("favourites", i.title)
        }
        val layoutManager = LinearLayoutManager(activity!!.applicationContext, LinearLayoutManager.VERTICAL, false)
        favouritesListLayout.layoutManager = layoutManager
        val favouritesAdapter = FavouritesSongAdapter(GlobalClass.favourites, this)
        favouritesListLayout.adapter = favouritesAdapter
    }
}
