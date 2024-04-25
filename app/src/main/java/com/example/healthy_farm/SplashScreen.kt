package com.example.healthy_farm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager

class SplashScreen : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler(Looper.getMainLooper()).postDelayed({

            sharedPref = getSharedPreferences("userdata", Context.MODE_PRIVATE)

            val isLogin = sharedPref.getBoolean("isUserLoggedIn", false)
            val whType = sharedPref.getString("type","")
            if (isLogin == true) {
                if(whType=="Customer") {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this, FarmerHome::class.java)
                    startActivity(intent)
                    finish()
                }
            }else{
                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)

    }
}