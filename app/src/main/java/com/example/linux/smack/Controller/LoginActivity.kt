package com.example.linux.smack.Controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.linux.smack.R
import com.example.linux.smack.Sevices.AuthService
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import org.jetbrains.anko.contentView

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.GONE
        val view = contentView!!

        setupListeners(view)
    }


    private fun setupListeners(view : View) {
        view.backgroundColorBtn.setOnClickListener{
            if (!isPasswordValid(loginPasswordText.text!!)) {
                textInputLayoutEmail.error = "Sua senha deve conter pelo menos 6 caracteres"
            }
            if (!isPasswordValid(loginEmailTxt.text!!)) {
                textInputLayout.error = "Seu email deve conter pelo menos 6 caracteres"
            }
            if  (isPasswordValid(loginEmailTxt.text!!) && isPasswordValid(loginPasswordText.text!!)){
                textInputLayoutEmail.error = null
                loginLoginBtnClicked(view)
            }
        }

        view.loginCreateUserBtn.setOnClickListener{
            loginCreateUserBtnClicked(view)
        }

        val tw = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (isPasswordValid(loginPasswordText.text!!)) {
                    textInputLayoutEmail.error = null
                }
                if (isPasswordValid(loginEmailTxt.text!!)) {
                    textInputLayout.error = null
                }
            }
        }

        view.loginPasswordText.addTextChangedListener(tw)
        view.loginEmailTxt.addTextChangedListener(tw)
    }


    private fun isPasswordValid(text: Editable?): Boolean {
        return text != null && text.length >= 6
    }

    fun loginLoginBtnClicked(view : View) {
        enableSpinner(true)
        val email = loginEmailTxt.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyBoard()
        if(email.isNotEmpty() and password.isNotEmpty()){
            AuthService.loginUser(email, password){ loginSuccess ->
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
            enableSpinner(false)
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
        backgroundColorBtn.isEnabled = !enable
        loginCreateUserBtn.isEnabled = !enable
    }

    fun hideKeyBoard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
