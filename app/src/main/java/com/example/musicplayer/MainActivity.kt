package com.example.musicplayer

import android.Manifest
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.CalendarContract.Attendees.query
import android.provider.CalendarContract.EventDays.query
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.example.musicplayer.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var navMenu: BottomNavigationView
    private lateinit var activeTrack : TextView
    private var menuItemID : MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navMenu = findViewById(R.id.bottomNavigationView)
        activeTrack = findViewById(R.id.activeTrack)
        val globalVariable: GlobalClass = GlobalClass.objectInstance
        val sharedPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val favouritesJson = sharedPrefs.getString("favourites", "")
        val playlistsJson = sharedPrefs.getString("playlists", "")
        val type = object : TypeToken<MutableList<GlobalClass.Music>>() {}.type
        val type1 = object : TypeToken<MutableMap<String, MutableList<GlobalClass.Music>>>() {}.type
        if(favouritesJson != "") {
            GlobalClass.favourites = gson.fromJson(favouritesJson, type) as MutableList<GlobalClass.Music>
        }
        else {
            Log.d("saving", "empty")
        }
        if(playlistsJson != "") {
            GlobalClass.playlists = gson.fromJson(playlistsJson, type1) as MutableMap<String, MutableList<GlobalClass.Music>>
        }
        else {
            Log.d("saving", "empty")
        }
        setActiveTrack()
        activeTrack.setOnClickListener {
            changeFragmentToMusic(MusicControllerFragment())
        }
        globalVariable.setContext(this)
        globalVariable.getMusic()
        navMenu.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.all_songs -> {
                    val allSongsFragment = AllSongsFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frag_container, allSongsFragment)
                        .addToBackStack(null)
                        .commit()
                }
                R.id.all_playlists -> {
                    val viewPlaylistFragment = ViewAllPlaylistsFragments()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frag_container, viewPlaylistFragment)
                        .addToBackStack(null)
                        .commit()
                    Toast.makeText(this, "All playlists", Toast.LENGTH_SHORT).show()
                }
                R.id.home_menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frag_container, HomeFragment())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frag_container, FavouritesFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            menuItem.isChecked = true
            true
        }
        supportFragmentManager.beginTransaction().replace(R.id.frag_container, HomeFragment())
            .addToBackStack(null)
            .commit()
    }

    fun changeFragmentToMusic(fragment: MusicControllerFragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frag_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun setActiveTrack() {
        if(GlobalClass.currentPlaying == null) {
            activeTrack.visibility = View.GONE
        }
        else {
            activeTrack.visibility = View.VISIBLE
            activeTrack.text = GlobalClass.currentPlaying!!.title
        }
    }

    fun setCurrentFragment() {
        when(supportFragmentManager.findFragmentById(R.id.frag_container)) {
            is AllSongsFragment -> {
                Log.d("frags", "changing")
                navMenu.menu.getItem(1).isChecked=true
            }
            is ViewAllPlaylistsFragments -> {
                Log.d("frags", "changing")
                navMenu.menu.getItem(3).isChecked=true
            }
            is FavouritesFragment -> {
                Log.d("frags", "changing")
                navMenu.menu.getItem(2).isChecked=true
            }
            is HomeFragment -> {
                Log.d("frags", "changing")
                navMenu.menu.getItem(0).isChecked=true
            }
        }
    }

    override fun onStop() {
        super.onStop()
        val globalVar: GlobalClass = GlobalClass.objectInstance
        globalVar.releasePlayer()
        val sharedPrefs = this.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPrefs.edit()
        val gson = Gson()
        val jsonFavourites : String = gson.toJson(GlobalClass.favourites)
        val playlists : String = gson.toJson(GlobalClass.playlists)
        editor.putString("playlists", playlists)
        Log.d("saving", jsonFavourites)
        editor.putString("favourites", jsonFavourites)
        editor.apply()
        editor.commit()
        Log.d("saving", "saved")
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1) {
            val sharedPrefs = this.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = sharedPrefs.edit()
            val gson = Gson()
            val jsonFavourites : String = gson.toJson(GlobalClass.favourites)
            Log.d("saving", jsonFavourites)
            editor.putString("favourites", jsonFavourites)
            editor.apply()
            editor.commit()
            exitProcess(0)
        }
        else {
            supportFragmentManager.popBackStack()
        }
    }

}
