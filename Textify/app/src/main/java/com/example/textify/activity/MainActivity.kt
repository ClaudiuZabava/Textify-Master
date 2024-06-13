package com.example.textify.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.textify.R
import com.example.textify.databinding.ActivityMainBinding
import com.example.textify.fragments.ChatsFragment
import com.example.textify.fragments.ContactsFragment
import com.example.textify.fragments.SettingsFragment
import com.example.textify.utils.Constants
import com.example.textify.utils.PreferenceHandler
import nl.joery.animatedbottombar.AnimatedBottomBar
import nl.joery.animatedbottombar.AnimatedBottomBar.OnTabSelectListener


class MainActivity : AppCompatActivity() {

    private lateinit var preferenceHandler: PreferenceHandler
    private lateinit var bindingMain: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        preferenceHandler = PreferenceHandler(applicationContext)

        installSplashScreen().apply {
            setKeepOnScreenCondition { true }
            Handler(Looper.getMainLooper()).postDelayed({

                if (!(preferenceHandler.getBoolean(Constants.KEY_IS_SIGNED_IN))) {
                    val intent = Intent(this@MainActivity, AuthActivity::class.java)
                    setKeepOnScreenCondition { false }
                    startActivity(intent)
                } else {
                    setKeepOnScreenCondition { false }
                }
            }, 1000)
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        ViewCompat.setOnApplyWindowInsetsListener(bindingMain.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(bindingMain.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController


        setListeners()

        val bottomBar = bindingMain.bottomBar
        bottomBar.setOnTabSelectListener(object: AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                navigateToDestination(newIndex)
            }
        })
    }

    private fun setListeners() {
        val searchIcon = bindingMain.searchIcon;
        val searchView = bindingMain.searchBar;

        searchIcon.setOnClickListener()
        {
            if (searchView.visibility == View.GONE) {
                searchView.visibility = View.VISIBLE
                searchView.isIconified = false
            } else {
                searchView.visibility = View.GONE
                searchView.isIconified = true
            }
        }

        searchView.setOnCloseListener {
            searchView.visibility = View.GONE
            true
        }
    }
    private fun navigateToDestination(tabIndex: Int) {
        when (tabIndex) {
            0 -> navigateToFragment(ChatsFragment())
            1 -> navigateToFragment(ContactsFragment())
            2 -> navigateToFragment(SettingsFragment())
            // Add more cases if you have additional tabs
            else -> throw IllegalArgumentException("Invalid tab index: $tabIndex")
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(bindingMain.navHostFragment.id, fragment)
            .commit()
    }
}