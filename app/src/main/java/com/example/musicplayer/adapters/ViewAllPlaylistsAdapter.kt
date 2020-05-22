package com.example.musicplayer.adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.R
import com.example.musicplayer.fragments.ViewPlaylistFragment
import com.example.musicplayer.views.PlaylistCardString
import com.example.musicplayer.views.ViewAllPlaylistsItem

class ViewAllPlaylistsAdapter(private val list : MutableList<String>, val fragment: Fragment) : RecyclerView.Adapter<ViewAllPlaylistsItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllPlaylistsItem {
        val inflater = LayoutInflater.from(parent.context)
        return ViewAllPlaylistsItem(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewAllPlaylistsItem, position: Int) {
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
        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(fragment.activity!!)
            builder.setTitle("Deleting playlist")
            builder.setMessage("Are you sure you want to delete?")
            builder.setPositiveButton("Yes") { dialog, which ->
                list.remove(music)
                notifyItemRemoved(position)
                GlobalClass.playlists.remove(music)
            }
            builder.setNeutralButton("No") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
            true
        }
    }

    override fun getItemCount(): Int = list.size

}