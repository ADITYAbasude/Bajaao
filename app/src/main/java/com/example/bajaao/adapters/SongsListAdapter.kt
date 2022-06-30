package com.example.bajaao.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bajaao.R
import com.example.bajaao.activities.HomeActivity
import com.example.bajaao.activities.ViewSongActivity
import com.example.bajaao.constants.ConstantsFunc
import com.example.bajaao.databinding.AdapterSongsListBinding
import com.example.bajaao.fragments.HomeFragment
import com.example.bajaao.model.OfflineMusicModel

class SongsListAdapter(val context: Context, private val songsList: ArrayList<OfflineMusicModel>) :
    RecyclerView.Adapter<SongsListAdapter.ViewAdapter>() {

    private val viewSongActivity = ViewSongActivity::class.java

    class ViewAdapter(itemView: AdapterSongsListBinding) : RecyclerView.ViewHolder(itemView.root) {
        val songTitle: TextView = itemView.offlineSongTitle
        val songDuration: TextView = itemView.offlineSongDuration
        val songArtish: TextView = itemView.offlineSongArtish
        val offlineSongImage: ImageView = itemView.offlineSongImage
        val root = itemView.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdapter {
        return ViewAdapter(
            AdapterSongsListBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewAdapter, position: Int) {
        val songListData: OfflineMusicModel = songsList[position]

        holder.songTitle.text = songListData.title
        holder.songDuration.text = ConstantsFunc().convertTime(songListData.duration)
        holder.songArtish.text = songListData.artist

        Glide.with(context).load(songListData.imageUri)
            .apply(RequestOptions.placeholderOf(R.drawable.default_music_icon).centerCrop())
            .into(holder.offlineSongImage)


        holder.root.setOnClickListener {
            val intent = Intent(context, viewSongActivity)
            intent.putExtra("index", position)
            intent.putExtra("class", "songAdapter")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return songsList.size
    }
}