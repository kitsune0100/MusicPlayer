package com.example.musicplayer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass

import com.example.musicplayer.R
import com.example.musicplayer.adapters.createPlaylistAdapter

class CreatePlaylistFragment : Fragment() {

    private lateinit var createPlaylistRecycler : RecyclerView
    private lateinit var playlistNameText : EditText
    private lateinit var createButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPlaylistRecycler = view.findViewById(R.id.playlistSelectorRecycler)
        playlistNameText = view.findViewById(R.id.playlistNameEditText)
        createButton = view.findViewById(R.id.playlistCreateButton)
        val adapter = createPlaylistAdapter(GlobalClass.songList)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        createPlaylistRecycler.layoutManager = layoutManager
        createPlaylistRecycler.adapter = adapter
        createButton.setOnClickListener {
            val playlistName = playlistNameText.text.toString()
            val list = adapter.getList()
            if(playlistName.isEmpty() || list.isEmpty() || GlobalClass.playlists.containsKey(playlistName)) {
                Toast.makeText(activity, "INVALID", Toast.LENGTH_LONG).show()
            }
            else {
                Log.d("playlists", "playlist created")
                Log.d("playlists", "name $playlistName")
                GlobalClass.playlists[playlistName] = list
                for (i in GlobalClass.playlists[playlistName]!!) {
                    Log.d("playlists", "playlist track ${i.title}")
                }
                activity!!.supportFragmentManager.popBackStack()
            }
        }
    }
}
