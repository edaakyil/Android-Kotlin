package com.edaakyil.app.android.basicviews

import android.os.Bundle
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.edaakyil.app.android.basicviews.constant.REGISTER_INFO
import com.edaakyil.app.android.basicviews.constant.USERS_FILE_PATH
import com.edaakyil.app.android.basicviews.constant.USERS_FORMAT
import com.edaakyil.app.android.basicviews.model.RegisterInfoModel
import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

private const val REGISTER_USER_INFO_LOG_TAG = "REGISTER_USER_INFO"
private const val USER_INFO_EXISTS_LOG_TAG = "USER_INFO_EXISTS"

class RegisterPasswordActivity : AppCompatActivity() {
    private lateinit var mTextViewInfo: TextView
    private lateinit var mEditTextPassword: EditText
    private lateinit var mEditTextConfirmPassword: EditText
    private lateinit var mRegisterInfoModel: RegisterInfoModel

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

    private fun initialize() {
        // Versiyon kontrolü yapmak için Build sınıfını kullanıyoruz
        mRegisterInfoModel = when  {
            Build.VERSION.SDK_INT < 33 -> intent.getSerializableExtra(REGISTER_INFO) as RegisterInfoModel
            else -> intent.getSerializableExtra(REGISTER_INFO, RegisterInfoModel::class.java)!!
        }

        initViews()
    }

    private fun initViews() {
        mEditTextPassword = findViewById(R.id.registerPasswordActivityEditTextPassword)
        mEditTextConfirmPassword = findViewById(R.id.registerPasswordActivityEditTextConfirmPassword)
        initTexViewInfo()
    }

    private fun initTexViewInfo() {
        mTextViewInfo = findViewById(R.id.registerPasswordActivityTextViewInfo)
        mTextViewInfo.text = resources.getString(R.string.register_password_activity_text_view_info).format(mRegisterInfoModel.username)
    }

    // edaakyil
    private fun confirmPasswordAlertDialog() {
        Toast.makeText(this, R.string.alert_dialog_confirm_password_message, Toast.LENGTH_SHORT).show()

        AlertDialog.Builder(this)
            .setTitle(R.string.alert_dialog_close_title)
            .setMessage(R.string.alert_dialog_confirm_password_message)
            .setPositiveButton(R.string.alert_dialog_ok) { _, _ -> mEditTextPassword.text.clear(); mEditTextConfirmPassword.text.clear()}
            .create()
            .show()
    }

    private fun userExistsCallback(fis: FileInputStream): Boolean {
        var result = false

        try {
            while (true) {
                val ois = ObjectInputStream(fis)
                val registerInfo = ois.readObject() as RegisterInfoModel
                if (registerInfo.username == mRegisterInfoModel.username) {
                    result = true
                    break
                }
            }
        } catch (_: EOFException) {

        }

        return result
    }

    private fun userExists(): Boolean {
        var result = false

        try {
            result = FileInputStream(File(filesDir, USERS_FILE_PATH)).use(::userExistsCallback)
        } catch (ex: IOException) {
            Log.e(USER_INFO_EXISTS_LOG_TAG, ex.message ?: "")
            Toast.makeText(this, R.string.io_problem_occurred_prompt, Toast.LENGTH_SHORT).show()
        } catch (ex: Exception) {
            Log.e(USER_INFO_EXISTS_LOG_TAG, ex.message, ex)
            Toast.makeText(this, R.string.problem_occurred_prompt, Toast.LENGTH_SHORT).show()
        }

        return result
    }

    private fun registerUser() {
        try {
            ObjectOutputStream(FileOutputStream(File(filesDir, USERS_FILE_PATH), true)).use { it.writeObject(mRegisterInfoModel) }
            File(filesDir, USERS_FORMAT.format("${mRegisterInfoModel.username}.txt")).delete()
        } catch (ex: IOException) {
            Log.e(REGISTER_USER_INFO_LOG_TAG, ex.message ?: "")
            Toast.makeText(this, R.string.io_problem_occurred_prompt, Toast.LENGTH_SHORT).show()
        } catch (ex: Exception) {
            Log.e(REGISTER_USER_INFO_LOG_TAG, ex.message, ex)
            Toast.makeText(this, R.string.problem_occurred_prompt, Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUserInfo(password: String) {
        if (userExists()) {
            Toast.makeText(this, R.string.user_already_registered_prompt, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (mEditTextPassword.text.isBlank()) {
            Toast.makeText(this,R.string.password_missing_prompt, Toast.LENGTH_LONG).show()
            return
        }

        mRegisterInfoModel.password = password
        registerUser()
        Toast.makeText(this,R.string.user_successfully_registered_prompt, Toast.LENGTH_LONG).show()
        finish()
    }

    fun onRegisterButtonClicked(view: View) {
        val password = mEditTextPassword.text.toString()
        val confirmPassword = mEditTextConfirmPassword.text.toString()
        if (password == confirmPassword)
            registerUserInfo(password)
        else
            confirmPasswordAlertDialog()

        mEditTextPassword.text .clear()
        mEditTextConfirmPassword.text.clear()
    }

    fun onCloseButtonClicked(view: View) = finish()
}