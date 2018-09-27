package com.example.linux.smack.Sevices

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.linux.smack.App
import com.example.linux.smack.Model.Channel
import com.example.linux.smack.Utilities.URL_GET_CHANNEL
import org.json.JSONException

object MessageService {

    val channels = ArrayList<Channel>()

    fun getChannels(complete : (Boolean) -> Unit){

        val channelRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNEL,
                null, Response.Listener{ response ->
            try {
                for (x in 0 until response.length()){
                    Log.e("try", "nume: " + x )
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val description = channel.getString("description")
                    val id = channel.getString("_id")

                    val newChannel = Channel(name, description, id)
                    this.channels.add(newChannel)
                }
                complete(true)

            }catch (e : JSONException){
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener { error ->
            Log.e("Error", "Could not retrieve channels: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }
        App.prefs.requestQueue.add(channelRequest)
    }
}