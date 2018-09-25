package com.example.linux.smack.Sevices

import android.graphics.Color
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
        AuthService.authToken = ""
        AuthService.userEmail = ""
        AuthService.isLoggedIn = false
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
            r = (scanner.nextDouble()).toInt()
            g = (scanner.nextDouble()).toInt()
            b = (scanner.nextDouble()).toInt()
        }
        return Color.rgb(r, g, b)
    }

}