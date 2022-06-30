package com.example.bajaao.services

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.MediaSession2
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.bajaao.R
import com.example.bajaao.activities.ViewSongActivity
import com.example.bajaao.receivers.ApplicationClass

class MusicServices : Service() {
    val myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(this , "Bajaao Song App")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentServices(): MusicServices {
            return this@MusicServices
        }
    }

    fun showNotification() {
        val notification = NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID)
            .setContentTitle(ViewSongActivity.songDataList[0].title)
            .setContentText(ViewSongActivity.songDataList[0].artist)
            .setSmallIcon(R.drawable.default_music_icon)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.default_music_icon))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken)) // passing a unique token to reduce a ambiguity rate because same time two or more music app is running.
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.skip_previous, "Previous", null)
            .addAction(R.drawable.pause_circle, "Pause", null)
            .addAction(R.drawable.skip_next, "Next", null)
            .build()

        startForeground(1 , notification)
    }
}