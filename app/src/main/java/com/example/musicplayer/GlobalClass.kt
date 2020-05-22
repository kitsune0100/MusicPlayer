package com.example.musicplayer

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaTimestamp
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList
import kotlin.time.Duration
import kotlin.time.milliseconds

class GlobalClass : Application() {

    data class Music(
        val title: String,
        val id: Long,
        val duration: Long,
        val contentURI: String,
        var isHeartSelected: Int
    ) : Serializable
    {
        override fun toString(): String {
            return super.toString()
        }
    }

    private var musicPlayer: MediaPlayer? = null
    private var instance: Context = this

    companion object {
        var currentIndex: Int = 0
        val songList: MutableList<Music> = mutableListOf()
        val songNames: ArrayList<String> = arrayListOf()
        var recentlyPlayed: LinkedList<Music> = LinkedList()
        var favourites: MutableList<Music> = mutableListOf()
        var playlists : MutableMap<String, MutableList<Music>> = mutableMapOf()
        var currentPlaying: Music? = null
        var currentPlaylist: MutableList<Music> = mutableListOf()
        val objectInstance = GlobalClass()
    }

    fun setContext(context: Context) {
        instance = context
    }

    fun addToFavourites(music: Music) {
        if (!favourites.contains(music) && !favourites.any { it.title == music.title }) {
            songList[songList.indexOf(music)].isHeartSelected=1
            favourites.add(music)
        }
    }

    fun removeFromFavourites(music: Music) {
        if (favourites.contains(music)) {
            songList[songList.indexOf(music)].isHeartSelected=0
            favourites.remove(music)
        }
        favourites.removeAll {
            it.title==music.title
        }
    }

    fun addToRecentlyPlayed(music: Music) {
        if (recentlyPlayed.size == 10) {
            recentlyPlayed.removeAt(0)
            recentlyPlayed.addFirst(music)
        } else {
            recentlyPlayed.addFirst(music)
        }
    }

    fun getMusic() {
        val contentResolver: ContentResolver? = instance.contentResolver
        val isMusic = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            isMusic,
            null,
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val nameID: Int = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val idID: Int = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val durationID: Int = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            do {
                val title = cursor.getString(nameID)
                val id = cursor.getLong(idID)
                val duration = cursor.getLong(durationID)
                val contentUri: Uri =
                    ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                songList.add(
                    Music(
                        title.substring(0, title.indexOf('.')),
                        id,
                        duration,
                        contentUri.toString(),
                        0
                    )
                )
                songNames.add(title.substring(0, title.indexOf('.')))
                Log.d("result", "title ${title.substring(0, title.indexOf('.'))}")
            } while (cursor.moveToNext())
        }
        Log.d("result", cursor?.count.toString())
        cursor?.close()
    }

    fun playMusic(variable: Music) {
        releasePlayer()
        musicPlayer = MediaPlayer.create(instance, Uri.parse(variable.contentURI))
        currentPlaying = variable
//        (instance as MainActivity).setActiveTrack()
        musicPlayer?.start()
        Log.d("progressindex", "currently playing at index $currentIndex")
        musicPlayer?.setOnCompletionListener {
            Log.d("progress", "releasing")
            if (musicPlayer!!.isLooping) {
                releasePlayer()
                playMusic(currentPlaying!!)
            } else {
                playNextSong()
            }
        }
    }

    fun playNextSong() {
        if (currentIndex == currentPlaylist.size-1 && isLooping()) {
            currentIndex = 0
            playMusic(currentPlaylist[currentIndex])
        } else if (currentIndex == currentPlaylist.size-1 && !isLooping()) {
            currentIndex = 0
            currentPlaying = null
            currentPlaylist.clear()
            releasePlayer()
        } else {
            currentIndex++
            playMusic(currentPlaylist[currentIndex])
        }
    }

    fun playPreviousSong() {
        if(currentIndex == 0) {
            currentIndex = currentPlaylist.size-1
            playMusic(currentPlaylist[currentIndex])
        }
        else {
            currentIndex--
            playMusic(currentPlaylist[currentIndex])
        }
    }

    fun clearPlaylist() {
        currentPlaylist.clear()
    }

    fun isPlaylistClear(): Boolean {
        if (currentPlaylist.size == 0)
            return true
        return false
    }

    fun hasStopped(): Boolean {
        return currentPlaying == null
    }

    fun releasePlayer() {
        musicPlayer?.release()
        musicPlayer = null
    }

    fun activateLoop() {
        musicPlayer?.isLooping = true
    }

    fun deactivateLoop() {
        musicPlayer?.isLooping = false
    }

    fun isLooping(): Boolean {
        return musicPlayer!!.isLooping
    }

    fun pausePlayer() {
        musicPlayer?.pause()
    }

    fun resumePlayer() {
        musicPlayer?.start()
    }

    fun getCurrentTime(): Long {
        return musicPlayer!!.currentPosition.toLong()
    }

    fun isPlaying(): Boolean {
        Log.d("progress", "inside is playing")
        return musicPlayer!!.isPlaying
    }

    fun seekTo(value: Int) {
        musicPlayer!!.seekTo(value)
    }

}