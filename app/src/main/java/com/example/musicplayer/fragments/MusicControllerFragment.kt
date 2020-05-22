package com.example.musicplayer.fragments

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.view.GestureDetectorCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.musicplayer.GlobalClass
import com.example.musicplayer.MainActivity

import com.example.musicplayer.R
import com.example.musicplayer.models.Utilities
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.abs

class MusicControllerFragment : Fragment() {

    lateinit var currentImage: TextView
    lateinit var currentTitle: TextView
    lateinit var seekBar: SeekBar
    lateinit var pauseButton: ImageButton
    lateinit var loopButton: ImageButton
    lateinit var shuffleButton: ImageButton
    lateinit var forwardButton: ImageButton
    lateinit var backwardButton: ImageButton
    var globalObject = GlobalClass.objectInstance
    var isPlaylistChanged = 0
    var utils = Utilities()
    lateinit var currentTimeText: TextView
    lateinit var totalTimeText: TextView
    lateinit var heartImage: ImageView
    val mHandler = Handler()
    val mIsPlayingTaskHandler = Handler()
    private val current: GlobalClass.Music? = GlobalClass.currentPlaying

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music_controller, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentImage = view.findViewById(R.id.controllerImage)
        currentTitle = view.findViewById(R.id.controllerTitle)
        seekBar = view.findViewById(R.id.controllerSeek)
        pauseButton = view.findViewById(R.id.controllerPause)
        loopButton = view.findViewById(R.id.controllerLoop)
        heartImage = view.findViewById(R.id.imageView4)
        currentTimeText = view.findViewById(R.id.controllerCurrentTime)
        totalTimeText = view.findViewById(R.id.controllerTotalTime)
        shuffleButton = view.findViewById(R.id.controllerShuffle)
        forwardButton = view.findViewById(R.id.controllerForward)
        backwardButton = view.findViewById(R.id.controllerBackward)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val total: Long = current!!.duration
                val currentTime: Long = globalObject.getCurrentTime()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (globalObject.hasStopped()) {
                    return
                }
                mHandler.removeCallbacks(mUpdateTimeTask)
                val maxDuration = (seekBar!!.progress * 1000).toInt()
                globalObject.seekTo(maxDuration)
                updateSeekbar()
            }

        })
        if (!globalObject.isPlaylistClear())
            mIsPlayingTaskHandler.postDelayed(mIsPlayingTask, 100)
        setListeners()
        setDetails()
    }

    private fun setListeners() {
        heartImage.setOnClickListener {
            if(GlobalClass.favourites.any {
                    it.title == GlobalClass.currentPlaying!!.title
                }) {
                val indexOfMusic = GlobalClass.favourites.find {
                    it.title == GlobalClass.currentPlaying!!.title
                }
                GlobalClass.favourites.remove(indexOfMusic)
                GlobalClass.currentPlaying!!.isHeartSelected = 0
                heartImage.setImageResource(R.drawable.ic_heart)
            }
            else {
                GlobalClass.favourites.add(GlobalClass.currentPlaying!!)
                GlobalClass.currentPlaying!!.isHeartSelected = 1
                heartImage.setImageResource(R.drawable.ic_untitled)
            }
        }
        pauseButton.setOnClickListener {
            if (globalObject.isPlaying()) {
                Log.d("progress", "pausing")
                pauseButton.setImageResource(R.drawable.ic_resume)
                globalObject.pausePlayer()
            } else {
                Log.d("progress", "resuming")
                pauseButton.setImageResource(R.drawable.ic_pause)
                globalObject.resumePlayer()
            }
        }
        with(currentImage) { setOnTouchListener(OnSwipeTouchListenerMusicImage(context)) }
        shuffleButton.setOnClickListener {
            if(isPlaylistChanged == 0) {
                GlobalClass.currentPlaylist.shuffle()
                for (i in GlobalClass.currentPlaylist) {
                    Log.d("shuffledebug", " cuurent : ${i.title}")
                }
                for(i in GlobalClass.originalPlaylist) {
                    Log.d("shuffledebug", "original : ${i.title}")
                }
                shuffleButton.setColorFilter(context!!.resources.getColor(android.R.color.holo_green_light))
                isPlaylistChanged = 1
            }
            else if(isPlaylistChanged == 1) {
                for(i in 0 until GlobalClass.currentPlaylist.size) {
                    GlobalClass.currentPlaylist[i]=GlobalClass.originalPlaylist[i]
                }
                for (i in GlobalClass.currentPlaylist) {
                    Log.d("shuffledebug", " cuurent : ${i.title}")
                }
                for(i in GlobalClass.originalPlaylist) {
                    Log.d("shuffledebug", "original : ${i.title}")
                }
                shuffleButton.setColorFilter(context!!.resources.getColor(android.R.color.white))
                isPlaylistChanged = 0
            }
        }
        loopButton.setOnClickListener {
            if (globalObject.isLooping()) {
                globalObject.deactivateLoop()
                loopButton.setColorFilter(context!!.resources.getColor(android.R.color.white))
            } else {
                globalObject.activateLoop()
                loopButton.setColorFilter(context!!.resources.getColor(android.R.color.holo_green_light))
            }
        }
        forwardButton.setOnClickListener {
            YoYo.with(Techniques.SlideOutLeft)
                .duration(500)
                .onStart {
                    val globalObject = GlobalClass.objectInstance
                    globalObject.playNextSong()
                }
                .onEnd {
                    YoYo.with(Techniques.SlideInRight)
                        .duration(500)
                        .playOn(currentImage)
                }
                .playOn(currentImage)
        }
        backwardButton.setOnClickListener {
            YoYo.with(Techniques.SlideOutRight)
                .duration(500)
                .onStart {
                    val globalObject = GlobalClass.objectInstance
                    globalObject.playPreviousSong()
                }
                .onEnd {
                    YoYo.with(Techniques.SlideInLeft)
                        .duration(500)
                        .playOn(currentImage)
                }
                .playOn(currentImage)
        }
    }

    private val mIsPlayingTask = Runnable {
        stopIfNotPlaying()
    }

    private fun stopIfNotPlaying() {
        if (globalObject.isPlaylistClear()) {
            Log.d("callbacks", "removing callbacks")
            mIsPlayingTaskHandler.removeCallbacks(mIsPlayingTask)
            mHandler.removeCallbacks(mUpdateTimeTask)
            activity!!.supportFragmentManager.popBackStack()
        } else {
            mIsPlayingTaskHandler.postDelayed(mIsPlayingTask, 100)
        }
    }

    private val mUpdateTimeTask = object : Runnable {
        override fun run() {
            if (globalObject.hasStopped()) {
                Log.d("progress", "stopping update")
                return
            }
            Log.d("progress", "progress : ${(globalObject.getCurrentTime() / 1000).toInt()}")
            updateSeekbar()
        }
    }

    private fun setDetails() {
        with(currentTitle) {
            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
            setHorizontallyScrolling(true)
            marqueeRepeatLimit = -1
            isSelected = true
            text = GlobalClass.currentPlaying!!.title
        }
        currentTitle.text = GlobalClass.currentPlaying!!.title
        currentImage.text =
            GlobalClass.currentPlaying!!.title[0].toString().toUpperCase(Locale.ROOT)
        if (GlobalClass.currentPlaying!!.isHeartSelected == 0) {
            heartImage.setImageResource(R.drawable.ic_heart)
        } else {
            heartImage.setImageResource(R.drawable.ic_untitled)
        }
        updateSeekbar()
    }

    private fun updateSeekbar() {
        if (currentTitle.text.toString() != GlobalClass.currentPlaying!!.title) {
            with(currentTitle) {
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                setHorizontallyScrolling(true)
                marqueeRepeatLimit = -1
                isSelected = true
                text = GlobalClass.currentPlaying!!.title
            }
            currentImage.text =
                GlobalClass.currentPlaying!!.title[0].toString().toUpperCase(Locale.ROOT)
            if (GlobalClass.currentPlaying!!.isHeartSelected == 0) {
                heartImage.setImageResource(R.drawable.ic_heart)
            } else {
                heartImage.setImageResource(R.drawable.ic_untitled)
            }
        }
        seekBar.max = (GlobalClass.currentPlaying!!.duration / 1000).toInt()
        seekBar.progress = ((globalObject.getCurrentTime() / 1000).toInt())
        totalTimeText.text = utils.milliToTimes(GlobalClass.currentPlaying!!.duration)
        currentTimeText.text = utils.milliToTimes(globalObject.getCurrentTime())
        seekBar.refreshDrawableState()
        mHandler.postDelayed(mUpdateTimeTask, 1000)
    }

    inner class OnSwipeTouchListenerMusicImage(var context: Context) : View.OnTouchListener {
        private var gestureDetector: GestureDetectorCompat =
            GestureDetectorCompat(context, GestureListener(context))

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            return gestureDetector.onTouchEvent(event)
        }
    }

    inner class GestureListener(var context: Context) : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffY: Float = e2?.y!! - e1?.y!!
            val diffX: Float = e2.x - e1.x
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0)
                        onSwipeRight()
                    else
                        onSwipeLeft()
                }
            }
            return false
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        private fun onSwipeRight() {
            YoYo.with(Techniques.SlideOutRight)
                .duration(500)
                .onStart {
                    val globalObject = GlobalClass.objectInstance
                    globalObject.playPreviousSong()
                }
                .onEnd {
                    YoYo.with(Techniques.SlideInLeft)
                        .duration(500)
                        .playOn(currentImage)
                }
                .playOn(currentImage)
        }

        private fun onSwipeLeft() {
            YoYo.with(Techniques.SlideOutLeft)
                .duration(500)
                .onStart {
                    val globalObject = GlobalClass.objectInstance
                    globalObject.playNextSong()
                }
                .onEnd {
                    YoYo.with(Techniques.SlideInRight)
                        .duration(500)
                        .playOn(currentImage)
                }
                .playOn(currentImage)
        }

    }

    override fun onStop() {
        mHandler.removeCallbacks(mUpdateTimeTask)
        mIsPlayingTaskHandler.removeCallbacks(mIsPlayingTask)
        super.onStop()
    }

}
