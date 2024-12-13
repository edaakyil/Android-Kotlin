package com.edaakyil.app.android.firstapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy KK:mm:ss")
        val now = LocalDateTime.now()

        Toast.makeText(this, R.string.created_prompt, Toast.LENGTH_LONG).show()

        //Toast.makeText(this, "Created Date Time: ${formatter.format(now)}", Toast.LENGTH_LONG).show()
        //Toast.makeText(this, getString(R.string.created_date_time_prompt, formatter.format(now)), Toast.LENGTH_LONG).show()
        //Toast.makeText(this, resources.getString(R.string.created_date_time_prompt).format(formatter.format(now)), Toast.LENGTH_LONG).show()
        val message = resources.getString(R.string.created_date_time_prompt).format(formatter.format(now))
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}