package com.example.textify.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.textify.R
import com.example.textify.databinding.ActivitySplashScreenBinding
import com.example.textify.utils.Constants
import com.example.textify.utils.PreferenceHandler

class SplashActivity : AppCompatActivity() {
//    private var binding: ActivitySplashScreenBinding? = null
//    private lateinit var preferenceHandler: PreferenceHandler
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
//        preferenceHandler = PreferenceHandler(applicationContext)
//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        setContentView(binding?.root)
//        binding?.splashLottieIcon?.animation = AnimationUtils.loadAnimation(this,
//            R.anim.top_animation
//        )
//        binding?.splashTvTitle?.animation = AnimationUtils.loadAnimation(this,
//            R.anim.bottom_animation
//        )
//        binding?.splashTvSlogan?.animation = AnimationUtils.loadAnimation(this,
//            R.anim.bottom_animation
//        )
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            var intent = Intent(this@SplashActivity,AuthActivity::class.java)
//            if(preferenceHandler.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
//                intent = Intent(this@SplashActivity, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//            val pair1 = Pair<View, String>(binding?.splashLayout,binding?.splashLayout?.transitionName)
//            val pair2 = Pair<View, String>(binding?.splashLottieIcon,binding?.splashLottieIcon?.transitionName)
//            val pair3 = Pair<View, String>(binding?.splashTvTitle,binding?.splashTvTitle?.transitionName)
//            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@SplashActivity, pair1, pair2, pair3)
//            startActivity(intent,options.toBundle())
//            finish()
//        }, 2000)
//    }
}
