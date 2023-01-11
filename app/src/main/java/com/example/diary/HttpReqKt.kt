package com.example.diary

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.diary.Helper.*

class HttpReqKt {
    public fun loadDogImage(context :Context , url:String): String {
        val volleyQueue = Volley.newRequestQueue(context)
        val url = "https://dog.ceo/api/breeds/image/random"
        var res = "Failed to Get response"
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    val dogImageUrl = response.get("message")
                    res = dogImageUrl.toString()
                },
                { error ->
                    Log.e("MainActivity", "loadDogImage error: ${error.localizedMessage}")
                }
        )
        volleyQueue.add(jsonObjectRequest)
        return res;
    }
}