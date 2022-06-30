package com.example.bajaao.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.bajaao.R
import com.example.bajaao.fragments.HomeFragment
import com.example.bajaao.fragments.OfflineModeFragment
import com.example.bajaao.fragments.SearchFragment
import com.example.bajaao.fragments.YourLibraryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private var initHomeFragment = false
    private var initSearchFragment = false
    private var initYourLibraryFragment = false
    private var initGoOfflineMode = false

    private var homeFragment: HomeFragment? = null
    private var searchFragment: SearchFragment? = null
    private var yourLibraryFragment: YourLibraryFragment? = null
    private var offlineModeFragment: OfflineModeFragment? = null

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.hide()

        bottomNav = findViewById(R.id.bottomNav)


        val bundle = intent.extras

        if (bundle?.getString("offlineSongList") == "offlineSongList") {
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView2, OfflineModeFragment())
                .addToBackStack("offlineSongList")
                .commit()
        }
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
