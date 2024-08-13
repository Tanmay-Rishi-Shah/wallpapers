package com.example.ovflats

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SplashActiviy : AppCompatActivity() {
    private val splashTimeOut: Long = 1500 // 3 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_activiy)

        // Check login state of admin
        val sharedPreferences = getSharedPreferences("MyAdmin", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedInAdmin", false)

        // Check login state of user
        val sharedPreferencesUser = getSharedPreferences("MyUser", Context.MODE_PRIVATE)
        val isLoggedInUser = sharedPreferencesUser.getBoolean("isLoggedInUser", false)

        if (isLoggedIn) {
            Handler().postDelayed({
                startActivity(Intent(this, AdminDashboardActivity::class.java))
                finish()
            }, splashTimeOut)
        }
        else if(isLoggedInUser){
            Handler().postDelayed({
                startActivity(Intent(this, UserDashboardActivity::class.java))
                finish()
            }, splashTimeOut)
        }
        else {
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, splashTimeOut)
        }


    }
}
