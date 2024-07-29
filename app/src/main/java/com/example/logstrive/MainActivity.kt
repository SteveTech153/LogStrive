package com.example.logstrive

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (SessionManager.isLoggedIn(this)) {
            // User is logged in, proceed to main screen
//            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            // User is not logged in, proceed to login screen
//            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}