package com.example.textify.activity

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.textify.R
import com.example.textify.databinding.ActivityAuthBinding
import com.example.textify.fragment.LoginFragment
import com.example.textify.utils.Constants
import com.example.textify.utils.PreferenceHandler
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding;
    private lateinit var prefHandler: PreferenceHandler;
    private lateinit var auth: FirebaseAuth;



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        prefHandler = PreferenceHandler(applicationContext)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.auth) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportFragmentManager.beginTransaction().replace(R.id.login_holder, LoginFragment()).commit()

        //setListeners()

    }

//    private fun setListeners()
//    {
//
//        val btnLogin = binding.btnLogin;
//        val emailFld = binding.email;
//        val passwordFld = binding.password;
//
//        btnLogin.setOnClickListener()
//        {
//            Log.d("DEBUG ", "Passed here inside on click")
//            val email: String = emailFld.text.toString();
//            val password: String = passwordFld.text.toString();
//            if(isValidSignInDetails(email, password))
//            {
//                login(email,password)
//            }
//            else
//            {
//                showToast("Invalid email or password")
//            }
//        }
//
//    }
//
//    private fun login(email: String, password: String) {
//        auth = FirebaseAuth.getInstance()
//        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
//            if(it.isSuccessful) {
//                prefHandler.putBoolean(Constants.KEY_IS_SIGNED_IN,true)
//                prefHandler.putString(Constants.KEY_USER_ID,it.result.user?.uid!!)
//                val intent = Intent(this@AuthActivity, MainActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                startActivity(intent)
//                finish()
//            }
//            else
//            {
//                showToast("Unable to Sign In")
//            }
//        }
//    }
//
//    private fun isValidSignInDetails(email: String, password: String): Boolean {
//        return if(email.trim().isEmpty()) {
//            showToast("Enter Email")
//            false
//        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            showToast("Enter Valid Email")
//            false
//        } else if(password.trim().isEmpty()) {
//            showToast("Enter Password")
//            false
//        } else {
//            true
//        }
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
//    }
}