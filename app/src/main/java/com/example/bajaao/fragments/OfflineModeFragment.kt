package com.example.bajaao.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bajaao.R
import com.example.bajaao.activities.ViewSongActivity
import com.example.bajaao.adapters.SongsListAdapter
import com.example.bajaao.model.OfflineMusicModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class OfflineModeFragment : Fragment() {

    private lateinit var displaySongList: RecyclerView
    private var songsListAdapter: SongsListAdapter? = null
    private lateinit var totalOfflinetrack: TextView
    private lateinit var shuffleOfflineTracks: FloatingActionButton
    private lateinit var offlineSongsScrollView: NestedScrollView

    companion object {
        var songList = ArrayList<OfflineMusicModel>()
        private val viewSongActivity = ViewSongActivity::class.java
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_offline_mode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displaySongList = view.findViewById(R.id.displaySongList)
        totalOfflinetrack = view.findViewById(R.id.totalOfflinetracks)
        shuffleOfflineTracks = view.findViewById(R.id.shuffleOfflineTracks)
        offlineSongsScrollView = view.findViewById(R.id.offlineSongsScrollView)

        val fadeAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_animation)
        displaySongList.startAnimation(fadeAnim)



        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            songList = fetchSongs()
        }

        totalOfflinetrack.text = songList.size.toString() + " tracks"


        displaySongList.setHasFixedSize(true)
        songsListAdapter = SongsListAdapter(context!!, songList)
        displaySongList.layoutManager = LinearLayoutManager(requireContext())
        displaySongList.adapter = songsListAdapter
        displaySongList.isVerticalScrollBarEnabled = false


        shuffleOfflineTracks.setOnClickListener {
            val intent = Intent(context, ViewSongActivity::class.java)
            intent.putExtra("index", 0)
            intent.putExtra("class", "shuffleSongList")
            context!!.startActivity(intent)
//            Toast.makeText(context , "working" , Toast.LENGTH_SHORT).show()
        }

    }

    private fun fetchSongs(): ArrayList<OfflineMusicModel> {
        val tempList = ArrayList<OfflineMusicModel>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + " !=0"
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val cursor = requireContext().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null,
            null
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val title =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val artist =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val album =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val duration =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumId =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUri = Uri.withAppendedPath(uri, albumId.toString()).toString()

                    val offlineMusicModel =
                        OfflineMusicModel(title, path, id, album, artist, duration, artUri)

                    val file = File(offlineMusicModel.path)
                    if (file.exists()) {
                        tempList.add(offlineMusicModel)
                    }
                } while (cursor.moveToNext())
                cursor.close()
            }
        }

        return tempList
    }


}