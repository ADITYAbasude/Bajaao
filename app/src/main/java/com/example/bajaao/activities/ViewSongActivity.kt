package com.example.bajaao.activities

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.bajaao.R
import com.example.bajaao.constants.ConstantsFunc
import com.example.bajaao.fragments.OfflineModeFragment
import com.example.bajaao.model.OfflineMusicModel
import com.example.bajaao.services.MusicServices
import kotlinx.coroutines.Runnable
import kotlin.properties.Delegates


class ViewSongActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener , ServiceConnection {

    private lateinit var downTheActivity: ImageButton
    private lateinit var songImage: ImageView
    private lateinit var songTitle: TextView
    private lateinit var songArtist: TextView
    private lateinit var songTrack: SeekBar
    private lateinit var songCurrentTime: TextView
    private lateinit var songEndTime: TextView
    private lateinit var backSong: ImageButton
    private lateinit var play_pause_theSong: ImageButton
    private lateinit var nextSong: ImageButton
    private lateinit var viewFullSongScrollView: ScrollView
    private lateinit var shuffleTheNextSong: ImageButton
    private lateinit var repeatTheCurrentSong: ImageButton

    private var isPlaying: Boolean = true
    private lateinit var runnable: Runnable

    var songIndex: Int = 0

    companion object {
        lateinit var songDataList: ArrayList<OfflineMusicModel>
        private var songPlayer: MediaPlayer? = MusicServices().mediaPlayer
        private val constantsFunc = ConstantsFunc()
        private var isShuffling: Boolean? = true
        private var musicServices: MusicServices? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_song)

        val intent2 = Intent(this, MusicServices::class.java)
        bindService(intent2, this, BIND_AUTO_CREATE)
        startService(intent2)

        downTheActivity = findViewById(R.id.downTheActivity)
        songImage = findViewById(R.id.songImage)
        songTitle = findViewById(R.id.songTitle)
        songArtist = findViewById(R.id.songArtishs)
        songEndTime = findViewById(R.id.songEndTime)
        play_pause_theSong = findViewById(R.id.play_pause_theSong)
        songTrack = findViewById(R.id.songTrack)
        songCurrentTime = findViewById(R.id.songCurrentTime)
        backSong = findViewById(R.id.backSong)
        nextSong = findViewById(R.id.nextSong)
        viewFullSongScrollView = findViewById(R.id.viewFullSongScrollView)
        repeatTheCurrentSong = findViewById(R.id.repeatTheCurrentSong)
        shuffleTheNextSong = findViewById(R.id.shuffleTheNextSong)

        songTitle.isSelected = true
        songArtist.isSelected = true

        supportActionBar?.hide()


        createLayout()

        play_pause_theSong.setOnClickListener {
            isPlaying = if (isPlaying) {
                songPlayer!!.pause()
                play_pause_theSong.setImageResource(R.drawable.play_circle)
                false
            } else {
                songPlayer!!.start()
                play_pause_theSong.setImageResource(R.drawable.pause_circle)
                true
            }
        }

        songPlayer?.setOnCompletionListener(this)

        backSong.setOnClickListener {
            movePreviousSong()
        }

        nextSong.setOnClickListener {
            moveNextSong()
        }

        downTheActivity.setOnClickListener {
            onBackPressed()
        }

        shuffleTheNextSong.setOnClickListener {
            shuffleTheNextSong
        }

        shuffleTheSongList()

    }

    private fun createMediaPlayer() {
        if (songPlayer == null) songPlayer = MediaPlayer()
        songPlayer!!.reset()
        songPlayer!!.setDataSource(songDataList[songIndex].path)
        songPlayer!!.prepare()
        play_pause_theSong.setImageResource(R.drawable.pause_circle)
        songPlayer!!.start()
        isPlaying = true
    }

    private fun shuffleTheSongList() {
        isShuffling = if (isShuffling == true) {
            val porterDuffColorFilter = PorterDuffColorFilter(
                R.color.white,
                PorterDuff.Mode.SRC_ATOP
            )
            shuffleTheNextSong.drawable.colorFilter = porterDuffColorFilter
            false
        } else {
            songDataList.shuffle()
            val porterDuffColorFilter = PorterDuffColorFilter(
                R.color.orange,
                PorterDuff.Mode.SRC_ATOP
            )
            shuffleTheNextSong.drawable.colorFilter = porterDuffColorFilter
            true
        }
    }

    private fun createLayout() {
        songIndex = intent.getIntExtra("index", 0)
        when (intent?.getStringExtra("class")) {
            "songAdapter" -> {
                songDataList = ArrayList()
                songDataList.addAll(OfflineModeFragment.songList)
                createMediaPlayer()
                layoutInitializer(songIndex)
                seekBarInitializer()
            }
            "shuffleSongList" -> {
                songDataList = ArrayList()
                songDataList.addAll(OfflineModeFragment.songList)
                songDataList.shuffle()
                createMediaPlayer()
                layoutInitializer(songIndex)
                seekBarInitializer()
            }
        }
    }

    private fun layoutInitializer(songIndex: Int) {
        Glide.with(this).load(songDataList[songIndex].imageUri)
            .apply(RequestOptions.placeholderOf(R.drawable.default_music_icon).centerCrop())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(this@ViewSongActivity, "Image fail to load", Toast.LENGTH_SHORT)
                        .show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Palette.from(resource!!.toBitmap()).generate { palette ->
                        palette?.let {
                            val color = it.mutedSwatch?.rgb ?: 0
                            val drawable = GradientDrawable().apply {
                                colors = intArrayOf(color, R.color.black)
                                orientation = GradientDrawable.Orientation.TOP_BOTTOM
                                gradientType = GradientDrawable.LINEAR_GRADIENT
                            }
                            viewFullSongScrollView.setBackgroundDrawable(drawable)
                            songTrack.progressTintList =
                                ColorStateList.valueOf(it.lightVibrantSwatch?.rgb ?: 0)
                            songTrack.thumbTintList =
                                ColorStateList.valueOf(it.vibrantSwatch?.rgb ?: 0)
                        }
                    }
                    return false
                }
            })
            .into(songImage)

        songTitle.text = songDataList[songIndex].title
        songArtist.text = songDataList[songIndex].artist
        songEndTime.text = constantsFunc.convertTime(songDataList[songIndex].duration)
    }

    private fun seekBarInitializer() {
        songTrack.max = songPlayer!!.duration
        songTrack.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                songCurrentTime.text =
                    constantsFunc.convertTime(songPlayer!!.currentPosition.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                songCurrentTime.text =
                    constantsFunc.convertTime(songPlayer!!.currentPosition.toLong())
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                songPlayer!!.seekTo(songTrack.progress)
                play_pause_theSong.setImageResource(R.drawable.pause_circle)
                songPlayer!!.start()
            }

        })

        runnable = Runnable {
            songCurrentTime.text =
                constantsFunc.convertTime(songPlayer!!.currentPosition.toLong())
            songTrack.progress = songPlayer!!.currentPosition.toLong().toInt()
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }

    private fun moveNextSong() {
        if (songDataList.size.minus(1) != songIndex) {
            songIndex++
            layoutInitializer(songIndex)
            createMediaPlayer()
            seekBarInitializer()
        } else {
            songIndex = 0
            layoutInitializer(songIndex)
            createMediaPlayer()
            seekBarInitializer()
        }
    }

    private fun movePreviousSong() {
        if (songIndex != 0) {
            --songIndex
            createMediaPlayer()
            layoutInitializer(songIndex)
            seekBarInitializer()
        } else {
            songIndex = songDataList.size - 1
            createMediaPlayer()
            layoutInitializer(songIndex)
            seekBarInitializer()
        }
        Toast.makeText(this, songIndex.toString(), Toast.LENGTH_SHORT).show()

    }

    override fun onCompletion(mp: MediaPlayer?) {
        moveNextSong()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("offlineSongList", "offlineSongList")
        overridePendingTransition(R.anim.activity_in_ani, R.anim.activity_out_ani)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicServices.MyBinder
        musicServices = binder.currentServices()
        createLayout()
        createMediaPlayer()
        layoutInitializer(songIndex)
        seekBarInitializer()
        Toast.makeText(this, songIndex.toString(), Toast.LENGTH_SHORT).show()
//        musicServices!!.showNotification()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicServices = null
    }

}