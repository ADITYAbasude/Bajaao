package com.example.bajaao.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.bajaao.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val user: String? = FirebaseAuth.getInstance().currentUser?.uid

        supportActionBar?.hide()


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            Handler().postDelayed({
                if (user != null) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, SimpleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 3000)
        } else {
            if (user != null) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, SimpleActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}

