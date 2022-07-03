package com.example.bajaao.services

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.bajaao.R
import com.example.bajaao.activities.ViewSongActivity
import com.example.bajaao.receivers.ApplicationClass

class MusicServices : Service() {
    private val myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext , "Bajaao Song App")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentServices(): MusicServices {
            return this@MusicServices
        }
    }

    fun showNotification() {
        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(ViewSongActivity.songDataList[ViewSongActivity.songIndex].title)
            .setContentText(ViewSongActivity.songDataList[ViewSongActivity.songIndex].artist)
//            .setSmallIcon(R.drawable.logo)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.default_music_icon))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken)) // passing a unique token to reduce a ambiguity rate because same time two or more music app is running.
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.skip_previous, "Previous", null)
            .addAction(R.drawable.pause_circle, "Pause", null)
            .addAction(R.drawable.skip_next, "Next", null)
            .build()
        Toast.makeText(this , "working" , Toast.LENGTH_SHORT).show()
        
        startForeground(13 , notification)
    }


//    fun seekBarSetup(){
//        runnable = kotlinx.coroutines.Runnable {
//            songCurrentTime.text =
//                ViewSongActivity.constantsFunc.convertTime(ViewSongActivity.songPlayer!!.currentPosition.toLong())
//            songTrack.progress = ViewSongActivity.songPlayer!!.currentPosition.toLong().toInt()
//            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
//        }
//        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
//    }

}