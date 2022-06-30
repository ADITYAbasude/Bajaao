package com.example.bajaao.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bajaao.R
import com.example.bajaao.fragments.LogInAndSignUpFragment

class SimpleActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        supportActionBar?.hide()

        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        val loginFragment = LogInAndSignUpFragment()
        fragmentTransition.replace(R.id.fragmentContainerView, loginFragment).commit()


    }
}