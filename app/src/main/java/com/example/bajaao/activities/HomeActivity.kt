package com.example.bajaao.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.bajaao.R
import com.example.bajaao.fragments.HomeFragment
import com.example.bajaao.fragments.OfflineModeFragment
import com.example.bajaao.fragments.SearchFragment
import com.example.bajaao.fragments.YourLibraryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.core.view.View

class HomeActivity : AppCompatActivity() {

    private lateinit var songBar: ConstraintLayout
    private lateinit var bottomNav: BottomNavigationView

    private var initHomeFragment = false
    private var initSearchFragment = false
    private var initYourLibraryFragment = false
    private var initGoOfflineMode = false

    private var homeFragment: HomeFragment? = null
    private var searchFragment: SearchFragment? = null
    private var yourLibraryFragment: YourLibraryFragment? = null
    private var offlineModeFragment: OfflineModeFragment? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.hide()

        bottomNav = findViewById(R.id.bottomNav)
        songBar = findViewById(R.id.songBar)


        val bundle = intent.extras

        if (bundle?.getString("offlineSongList") == "offlineSongList") {
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView2, OfflineModeFragment())
                .addToBackStack("offlineSongList")
                .commit()
        }

        songBar.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Toast.makeText(this , "down" , Toast.LENGTH_SHORT).show()
                }
                MotionEvent.ACTION_OUTSIDE -> {
                    Toast.makeText(this , "outside" , Toast.LENGTH_SHORT).show()
                }
            }
            return@setOnTouchListener true
        }
//
//        val sp: SharedPreferences = getSharedPreferences("lastSong" , MODE_PRIVATE)
//        val takePath =


    }


    fun homeClick(item: MenuItem) {
        if (!initHomeFragment) {
            initHomeFragment = false
            homeFragment = HomeFragment()
        }
        loadFragment(homeFragment!!, "HOME_FRAGMENT")
    }

    fun searchClick(item: MenuItem) {
        if (!initSearchFragment) {
            initHomeFragment = false
            searchFragment = SearchFragment()
        }
        loadFragment(searchFragment!!, "SEARCH_FRAGMENT")
    }

    fun yourLibraryClick(item: MenuItem) {
        if (!initYourLibraryFragment) {
            initYourLibraryFragment = false
            yourLibraryFragment = YourLibraryFragment()
        }
        loadFragment(yourLibraryFragment!!, "YOUR_LIBRARY_FRAGMENT")
    }

    fun goOfflineMode(item: MenuItem) {
        if (!initGoOfflineMode) {
            initGoOfflineMode = false
            offlineModeFragment = OfflineModeFragment()
        }
        loadFragment(offlineModeFragment!!, "OFFLINE_MODE_FRAGMENT")
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val currentFragment: Fragment? =
            fragmentManager.findFragmentById(R.id.fragmentContainerView2)


        if (currentFragment != null && currentFragment.javaClass.equals(fragment.javaClass)) {

        } else {
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView2, fragment).addToBackStack(tag)
                .commit()
        }
    }


}
