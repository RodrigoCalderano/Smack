package com.example.linux.smack.Controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.linux.smack.R
import com.example.linux.smack.Sevices.AuthService
import com.example.linux.smack.Sevices.UserDataService
import com.example.linux.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_create_user.view.*
import org.jetbrains.anko.contentView
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE

        val view = contentView!!

        setupListeners(view)
    }

    //TODO: ARRUMAR TRATAMENTO DE ERRO QUANTIDADE DE CARAC

    fun setupListeners(view : View){
        view.createAvatarImageView.setOnClickListener {
            generateUserAvatar()
            generateColorClicked()
        }
        view.createUserBtn.setOnClickListener {
            createUserCheck()
        }
    }

    fun generateUserAvatar(){
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)
        if (color == 0){
            userAvatar = "light$avatar"
        }
        else {
            userAvatar = "dark$avatar"
        }
        val resourceId = resources.getIdentifier(userAvatar,"drawable", packageName)
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateColorClicked(){
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)
        createAvatarImageView.setBackgroundColor(Color.rgb(r,g,b))
        avatarColor = "[$r, $g, $b, 1]"
    }

    fun createUserCheck(){
        if (!isPasswordValid(createPasswordText.text!!)) {
            textInputLayoutEmailPass.error = "Sua senha deve conter pelo menos 6 caracteres"
        }
        if (!isPasswordValid(createEmailText.text!!)) {
            textInputLayoutEmail.error = "Seu email deve conter pelo menos 6 caracteres"
        }
        if  (isPasswordValid(createPasswordText.text!!) && isPasswordValid(createEmailText.text!!)){
            textInputLayoutEmail.error = null
            textInputLayoutEmailPass.error = null
            createUserClicked()
        }


        val tw = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (isPasswordValid(createPasswordText.text!!)) {
                    textInputLayoutEmailPass.error = null
                }
                if (isPasswordValid(createEmailText.text!!)) {
                    textInputLayoutEmail.error = null
                }
            }
        }

        createPasswordText.addTextChangedListener(tw)
        createEmailText.addTextChangedListener(tw)
    }

    private fun isPasswordValid(text: Editable?): Boolean {
        return text != null && text.length >= 6
    }

    fun createUserClicked(){
        enableSpinner(true)
        val userName = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        if (userName.isNotEmpty() && email.isNotEmpty() and password.isNotEmpty()){
            AuthService.registerUser(email, password){ registerSuccess ->
                if(registerSuccess){
                    AuthService.loginUser(email, password){ loginSuccess ->
                        if(loginSuccess){
                            AuthService.createUser(userName, email, userAvatar, avatarColor)
                            {createSuccess ->
                                if(createSuccess){
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                                    enableSpinner(false)
                                    finish()
                                } else {
                                    errorToast("createSuccess")
                                }
                            }
                        } else {
                            errorToast("loginSuccess")
                        }
                    }
                } else {
                    errorToast("registerSuccess")
                }
            }
        } else{
            errorToast("Aparentemente algo em branco")
        }
    }

    fun errorToast(error : String) {
        enableSpinner(false)
        Toast.makeText(this, "Alguma coisa deu ruim: $error", Toast.LENGTH_LONG).show()
    }

    fun enableSpinner(enable : Boolean) {
        if(enable){
            createSpinner.visibility = View.VISIBLE
        } else{
            createSpinner.visibility = View.GONE
        }
        createUserBtn.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
    }

}
