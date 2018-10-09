package com.example.linux.smack.Sevices

import android.graphics.Color
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
        var r = 0
        var g = 0
        var b = 0
        val scanner = Scanner(strippedColor)
        if (scanner.hasNext()){
            //r = (scanner.nextDouble()).toInt()
            //g = (scanner.nextDouble()).toInt()
            //b = (scanner.nextDouble()).toInt()
        }
        return Color.rgb(200, 200, 200)
    }

}