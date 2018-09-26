package com.example.linux.smack.Controller

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.linux.smack.R
import com.example.linux.smack.Sevices.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.GONE
    }

    fun loginLoginBtnClicked(view : View) {
        enableSpinner(true)
        val email = loginEmailTxt.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyBoard()
        if(email.isNotEmpty() and password.isNotEmpty()){
            AuthService.loginUser(this, email, password){ loginSuccess ->
                if (loginSuccess){
                    AuthService.findUserByEmail(this){ findSuccess ->
                        if (findSuccess){
                            finish()
                            enableSpinner(false)
                        }else {
                            errorToast("findSuccess false")
                        }
                    }
                } else {
                    errorToast("loginSuccess false")
                }
            }
        }else{
            errorToast("Aparentemente algo em branco")
            enableSpinner(true)
        }
    }

    fun  loginCreateUserBtnClicked(view : View){
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun errorToast(error : String) {
        enableSpinner(false)
        Toast.makeText(this, "Alguma coisa deu ruim: $error", Toast.LENGTH_LONG).show()
    }

    fun enableSpinner(enable : Boolean) {
        if(enable){
            loginSpinner.visibility = View.VISIBLE
        } else{
            loginSpinner.visibility = View.GONE
        }
        loginLoginBtn.isEnabled = !enable
        loginCreateUserBtn.isEnabled = !enable
    }

    fun hideKeyBoard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
