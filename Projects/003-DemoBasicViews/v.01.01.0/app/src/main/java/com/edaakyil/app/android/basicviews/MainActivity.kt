package com.edaakyil.app.android.basicviews

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var mEditTextUsername: EditText
    private lateinit var mEditTextPassword: EditText
    private lateinit var mButtonLogin: Button
    private lateinit var mTextViewAcceptStatus: TextView
    private lateinit var mTextViewUserInfoStatus: TextView
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var mSwitchAccept: Switch

    private fun isUserInfoBlank() = mEditTextUsername.text.isBlank() || mEditTextPassword.text.isBlank()

    private fun doLogin() {
        //mSwitchAccept.text = resources.getText(R.string.main_activity_switch_accepted_text)
        mTextViewAcceptStatus.text = ""
        mTextViewUserInfoStatus.text = ""

        val username = mEditTextUsername.text.toString()
        val password = mEditTextPassword.text.toString()

        Toast.makeText(this, R.string.login_prompt, Toast.LENGTH_LONG).show()
        Toast.makeText(this, "$username, $password", Toast.LENGTH_LONG).show()
    }

    private fun loginButtonClickedCallback() {
        if (!mSwitchAccept.isChecked) {
            Toast.makeText(this, R.string.check_accept_message, Toast.LENGTH_SHORT).show()
            mTextViewAcceptStatus.text = resources.getString(R.string.check_accept_message)
            return
        }

        if (isUserInfoBlank()) {
            mTextViewUserInfoStatus.text = resources.getText(R.string.check_is_blank_message)
            return
        }

        doLogin()
    }

    private fun initLoginButton() {
        mButtonLogin = findViewById(R.id.mainActivityButtonLogin)
        mButtonLogin.setOnClickListener { loginButtonClickedCallback() }
    }

    private fun initViews() {
        mSwitchAccept = findViewById(R.id.mainActivitySwitchAccept)
        mEditTextUsername = findViewById(R.id.mainActivityEditTextUsername)
        mEditTextPassword = findViewById(R.id.mainActivityEditTextPassword)
        mTextViewAcceptStatus = findViewById(R.id.mainActivityTextViewAcceptStatus)
        mTextViewUserInfoStatus = findViewById(R.id.mainActivityTextViewUserInfoStatus)

        initLoginButton()
    }

    //private fun initialize() = initViews()
    private fun initialize() {
        initViews()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivityLinearLayoutMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialize()
    }

    fun onTitleTextClicked(view: View) {
        val text: TextView = findViewById(R.id.mainActivityTextViewLoginTitle)
        text.setTextColor(Color.parseColor("#DC7676"))

        Toast.makeText(this, R.string.main_activity_click_title_prompt, Toast.LENGTH_SHORT).show()
    }

    fun onAcceptSwitchClicked(view: View) {
        if (mSwitchAccept.isChecked) {
            mTextViewAcceptStatus.text = ""
            mSwitchAccept.text = "Accepted"
        } else
            mSwitchAccept.text = "Accept"
    }

    //fun onClearUsernameTextButtonClicked(view: View) = mEditTextUsername.setText("")
    fun onClearUsernameTextButtonClicked(view: View) {
        //mEditTextUsername.setText("")
        mEditTextUsername.text.clear()
    }

    //fun onClearPasswordTextButtonClicked(view: View) = mEditTextPassword.setText("")
    fun onClearPasswordTextButtonClicked(view: View) {
        //mEditTextPassword.text.clear()
        mEditTextPassword.setText("")
    }

    fun onClearAllButtonClicked(view: View) {
        onClearUsernameTextButtonClicked(view)
        onClearPasswordTextButtonClicked(view)
        mTextViewAcceptStatus.text = ""
    }

    fun onCloseButtonClicked(view: View) {
        // Toast.makeText(this, R.string.close_prompt, Toast.LENGTH_LONG).show()
        Toast.makeText(this, resources.getString(R.string.close_prompt), Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Destroying...", Toast.LENGTH_LONG).show()
    }
}