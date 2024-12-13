package com.edaakyil.app.android.basicviews

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.edaakyil.app.android.basicviews.constant.REGISTER_INFO
import com.edaakyil.app.android.basicviews.model.RegisterInfoModel

class RegisterPasswordActivity : AppCompatActivity() {
    private lateinit var mTextViewInfo: TextView
    private lateinit var mEditTextPassword: EditText
    private lateinit var mEditTextConfirmPassword: EditText
    private lateinit var mRegisterInfo: RegisterInfoModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerPasswordActivityLinearLayoutMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialize()
    }

    @SuppressLint("NewApi")
    private fun initialize() {
        initViews()
        mRegisterInfo = intent.getSerializableExtra(REGISTER_INFO, RegisterInfoModel::class.java)!!  // runtime'da exception oluşur.SuppressLint'i derleme zamanından geçmek için kullandık
    }

    private fun initViews() {
        mEditTextPassword = findViewById(R.id.registerPasswordActivityEditTextPassword)
        mEditTextConfirmPassword = findViewById(R.id.registerPasswordActivityEditTextConfirmPassword)
        initTexViewInfo()
    }

    private fun initTexViewInfo() {
        mTextViewInfo = findViewById(R.id.registerPasswordActivityTextViewInfo)
        mTextViewInfo.text = resources.getString(R.string.register_password_activity_text_view_info).format(mRegisterInfo.username)
    }

    fun onRegisterButtonClicked(view: View) {

    }

    fun onCloseButtonClicked(view: View) = finish()
}