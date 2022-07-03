package com.example.bajaao.deo

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.bajaao.constants.ApiUrls
import com.google.gson.Gson

class UserDeo {
    val apiData = ApiUrls()
    var user: String? = null


    fun userJsonRequest(method: Request.Method) {
        val userJsonObject = JsonObjectRequest(Request.Method.GET, apiData.USERS, null) { response ->
            val gson = Gson()
            user = gson.fromJson(response.toString())
        } {
            fun getHeaders(): Map<String , String> {

            }
        }
    }

}