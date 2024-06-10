package com.example.textify.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.textify.R
import com.example.textify.utils.Constants
import com.example.textify.utils.PreferenceHandler


class MainActivity : AppCompatActivity() {

    private lateinit var preferenceHandler: PreferenceHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        preferenceHandler = PreferenceHandler(applicationContext)

        installSplashScreen().apply {
            setKeepOnScreenCondition{ true }
            Handler(Looper.getMainLooper()).postDelayed({

                if(!(preferenceHandler.getBoolean(Constants.KEY_IS_SIGNED_IN)))
                {
                    val intent = Intent(this@MainActivity, AuthActivity::class.java)
                    setKeepOnScreenCondition { false }
                    startActivity(intent)
                }
                else{
                    setKeepOnScreenCondition { false }
                }
           }, 1000)
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}