package com.example.musicplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.GlobalClass.Companion.songList
import com.example.musicplayer.GlobalClass.Companion.songNames
import com.example.musicplayer.MainActivity
import com.example.musicplayer.R
import com.example.musicplayer.adapters.SongAdapter

class AllSongsFragment : Fragment() {

    private lateinit var songListLayout: ListView
    private lateinit var numberOfSongs : TextView
    private lateinit var autoComplete : AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_songs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setCurrentFragment()
        songListLayout = view.findViewById(R.id.songListLayout)
        numberOfSongs = view.findViewById(R.id.trackNumber)
        numberOfSongs.text = "${songList.size} Songs"
        autoComplete = view.findViewById(R.id.autoCompleteTextView)
        val adapter = ArrayAdapter<String>(activity!!, R.layout.autocompleteitem,songNames)
        autoComplete.setAdapter(adapter)
        autoComplete.setHorizontallyScrolling(true)
        autoComplete.setOnItemClickListener { parent, view, position, id ->
            val name : String? = adapter.getItem(position)
            setSongs(name)
        }
        autoComplete.setOnDismissListener {
            autoComplete.clearFocus()
        }
        autoComplete.addTextChangedListener {
            if(autoComplete.text.toString().isEmpty()) {
                setSongs()
            }
        }
        setSongs()
    }
    private fun setSongs(name : String?) {
        val tempList : MutableList<GlobalClass.Music> = mutableListOf()
        for (i in songList) {
            if(i.title == name) {
                tempList.add(i)
            }
        }
        val tempAdapter = SongAdapter(activity!!.applicationContext, tempList, this)
        songListLayout.adapter = tempAdapter
    }
    private fun setSongs() {
        val adapter = SongAdapter(activity!!.applicationContext, songList,this)
        songListLayout.adapter = adapter
    }
}

