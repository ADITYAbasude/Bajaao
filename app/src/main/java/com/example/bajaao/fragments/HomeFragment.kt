package com.example.bajaao.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.bajaao.R
import com.example.bajaao.activities.SettingActivity
import com.example.bajaao.constants.ApiUrls
import com.example.bajaao.volley.VolleySingleton
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*
import java.util.jar.Manifest
import kotlin.system.exitProcess

class HomeFragment : Fragment() {

    private lateinit var scrollLayout: ScrollView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fadeAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_animation)
        val settingsIcon: ImageView = view.findViewById(R.id.settings)
        scrollLayout = view.findViewById(R.id.scrollLayout)

        scrollLayout.startAnimation(fadeAnim)

        settingsIcon.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }

        val calendar = Calendar.getInstance()
        val currentTime = calendar.get(Calendar.HOUR_OF_DAY)

        val time: String = when (currentTime) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            in 21..24 -> "Good Night"
            else -> {}
        }.toString()

        val setTimeNews = view.findViewById<TextView>(R.id.setTimeNews)
        setTimeNews.text = time

//        getSongsFromApi()

        Dexter.withContext(context).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                p0.let {
                    if (p0?.areAllPermissionsGranted() == true) {

                    } else {
                        exitProcess(0)
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1!!.continuePermissionRequest()
            }
        }).check()


    }

    fun getSongsFromApi() {
        val url = ApiUrls().USERS

        val jsonObject = object : StringRequest(
            Request.Method.GET, url, Response.Listener<String> { response ->
                Log.e("data", response.substring(1, 500))
                Toast.makeText(context, response.substring(1, 100).toString(), Toast.LENGTH_SHORT)
                    .show()
            },
            Response.ErrorListener {
                Toast.makeText(context, "${it.networkResponse}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] =
                    "BQBU8kgfFpDdzUSKAt-ra1xIqQv57bqD9WnvUPfBc54gv30Ys7kmpWN_b6qXTtdIwxEchqSSfF6BQ7j0nQmFCxWqORU3VdHVxpHJz0mOWvm5ClR-mJ0XOW9yp_YN46uMgB6DoiI2jj_YoeM8nHZeAdNJiYvf_jR_8x7tf5Nw5v1CA1BYCvO2aMIp58jvMOQfqWJaGOi9"
                return headers

            }
        }

        VolleySingleton.getInstance(context!!).addToRequestQueue(jsonObject)

    }
}

