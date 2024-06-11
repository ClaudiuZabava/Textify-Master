package com.example.textify.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.textify.R
import com.example.textify.databinding.ActivityAuthBinding
import com.example.textify.databinding.FragmentLoginBinding
import com.example.textify.databinding.FragmentRegisterBinding
import com.example.textify.fragments.LoginFragment
import com.example.textify.utils.PreferenceHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthActivity : AppCompatActivity() {

    private lateinit var bindingAuth: ActivityAuthBinding;
    private lateinit var prefHandler: PreferenceHandler;
    private lateinit var auth: FirebaseAuth;
    private lateinit var fire: FirebaseFirestore;
    private lateinit var bindingLogin: FragmentLoginBinding;
    private lateinit var bindingRegister: FragmentRegisterBinding;



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingAuth = ActivityAuthBinding.inflate(layoutInflater)
        prefHandler = PreferenceHandler(applicationContext)
        setContentView(bindingAuth.root)
        ViewCompat.setOnApplyWindowInsetsListener(bindingAuth.auth) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.auth_holder, LoginFragment())
                .commit()
        }
    }
}