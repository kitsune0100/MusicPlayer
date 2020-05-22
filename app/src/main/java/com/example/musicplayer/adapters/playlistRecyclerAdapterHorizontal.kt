package com.example.musicplayer.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.R
import com.example.musicplayer.fragments.ViewPlaylistFragment
import com.example.musicplayer.views.PlaylistCard
import com.example.musicplayer.views.PlaylistCardString
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class playlistRecyclerAdapterHorizontal(private val list : MutableList<String>, val fragment: Fragment) : RecyclerView.Adapter<PlaylistCardString>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistCardString {
        val inflater = LayoutInflater.from(parent.context)
        return PlaylistCardString(inflater, parent)
    }

    override fun onBindViewHolder(holder: PlaylistCardString, position: Int) {
        val music = list[position]
        holder.bind(music)
        holder.itemView.setOnClickListener {
            val f = ViewPlaylistFragment()
            val args = Bundle()
            args.putString("key", music)
            f.arguments = args
            fragment.activity!!.supportFragmentManager.beginTransaction().replace(R.id.frag_container,f)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int = list.size

}