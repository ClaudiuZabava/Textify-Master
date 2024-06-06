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
import androidx.lifecycle.lifecycleScope
import com.example.textify.R
import com.example.textify.utils.Constants
import com.example.textify.utils.PreferenceHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition{ true }
//            lifecycleScope.launch { delay(1000L)
//                setKeepOnScreenCondition{ false }
//            }
            Handler(Looper.getMainLooper()).postDelayed({
                var intent = Intent(this@MainActivity,AuthActivity::class.java)
//                if(preferenceHandler.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
//                    intent = Intent(this@SplashActivity, MainActivity::class.java)
//                    ---> removed and putted outside      startActivity(intent)
//                  finish()
//               }
                setKeepOnScreenCondition{ false }
                startActivity(intent)
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