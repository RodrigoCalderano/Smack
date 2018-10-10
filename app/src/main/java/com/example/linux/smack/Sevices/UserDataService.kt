package com.example.linux.smack.Sevices

import android.graphics.Color
import android.util.Log
import com.example.linux.smack.App
import java.util.*

object UserDataService {

    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logout(){
        var id = ""
        var avatarColor = ""
        var avatarName = ""
        var email = ""
        var name = ""
        App.prefs.authToken = ""
        App.prefs.userEmail = ""
        App.prefs.isLoggedIn = false
        MessageService.clearMessages()
        MessageService.clearChannels()
    }

    fun returnAvatarColor(components : String) : Int {
        val strippedColor = components.replace("[", "")
                .replace("]", "")
                .replace(",", "")
                .replace(".0", "")

        val parts = strippedColor.split(" ")
        var r = 0
        var g = 0
        var b = 0
        try {
            r = parts[0].toInt()
            g = parts[1].toInt()
            b = parts[2].toInt()
        }catch (e : Exception){
            Log.e("UserDataService", "Color strip failed")
            Log.e("UserDataService", "Cor salva no db que causou erro: " + components)
        }
        return Color.rgb(r, g, b)
    }

}