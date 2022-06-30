package com.example.bajaao.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.bajaao.R
import com.example.bajaao.activities.HomeActivity
import com.example.bajaao.model.MusicDoYouLikeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SelectMusicDoYouLike : Fragment() {

    private lateinit var hindiSongs: TextView
    private lateinit var englishSongs: TextView
    private lateinit var teluguSongs: TextView
    private lateinit var punjabiSongs: TextView
    private lateinit var hindiSelected: ImageView
    private lateinit var englishSelected: ImageView
    private lateinit var teluguSelected: ImageView
    private lateinit var punjabiSelected: ImageView

    private lateinit var continueMusicLikePage: Button

    private val dataRef: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("User Info")
            .child(FirebaseAuth.getInstance().uid.toString()).child("MusicDoYouLike")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_music_do_you_like, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        textView
        hindiSongs = view.findViewById(R.id.hindiSongs)
        englishSongs = view.findViewById(R.id.englishSongs)
        teluguSongs = view.findViewById(R.id.teluguSongs)
        punjabiSongs = view.findViewById(R.id.punjabiSongs)

//        ImageView
        hindiSelected = view.findViewById(R.id.hindiSelected)
        englishSelected = view.findViewById(R.id.englishSelected)
        teluguSelected = view.findViewById(R.id.teluguSelected)
        punjabiSelected = view.findViewById(R.id.punjabiSelected)

//        Buttons
        continueMusicLikePage = view.findViewById(R.id.continueMusicLikePage)


        // variables
        var hindi: String? = null
        var english: String? = null
        var telugu: String? = null
        var punjabi: String? = null


//        logic part
        hindiSongs.setOnClickListener {

            hindiSelected.isVisible = !hindiSelected.isVisible
        }
        englishSongs.setOnClickListener {
            englishSelected.isVisible = !englishSelected.isVisible
        }
        teluguSongs.setOnClickListener {
            teluguSelected.isVisible = !teluguSelected.isVisible
        }
        punjabiSongs.setOnClickListener {
            punjabiSelected.isVisible = !punjabiSelected.isVisible
        }


        continueMusicLikePage.setOnClickListener {
            if (hindiSelected.isVisible) {
                hindi = "hindi"
            }
            if (englishSelected.isVisible) {
                english = "english"
            }
            if (teluguSelected.isVisible) {
                telugu = "telugu"
            }
            if (punjabiSelected.isVisible) {
                punjabi = "punjabi"
            }
            if (!punjabiSelected.isVisible) {
                punjabi = null
            }
            if (!teluguSelected.isVisible) {
                telugu = null
            }
            if (!englishSelected.isVisible) {
                english = null
            }
            if (!hindiSelected.isVisible) {
                hindi = null
            }

            val musicDoYouLike = MusicDoYouLikeModel(hindi , english , telugu , punjabi)
            dataRef.setValue(musicDoYouLike).addOnCompleteListener{
                if (it.isSuccessful){
                    val intent = Intent(requireActivity() , HomeActivity::class.java )
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }


    }
}


