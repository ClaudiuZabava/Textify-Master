package com.example.textify.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.textify.R
import com.example.textify.databinding.ActivityAuthBinding
import com.example.textify.databinding.FragmentLoginBinding
import com.example.textify.databinding.FragmentRegisterBinding
import com.example.textify.models.User
import com.example.textify.utils.Constants
import com.example.textify.utils.PreferenceHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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

        // binding for the login fragment
        bindingLogin = FragmentLoginBinding.inflate(layoutInflater)
        val fragmentView = bindingLogin.root
        findViewById<FrameLayout>(R.id.auth_holder).addView(fragmentView)

        setListeners()

    }

    private fun setListeners()
    {

        val btnLogin = bindingLogin.btnLogin;
        val emailFld = bindingLogin.email;
        val passwordFld = bindingLogin.password;
        val btnRegister = bindingLogin.btnRegister;

        btnLogin.setOnClickListener()
        {
            //Log.d("DEBUG ", "Passed here inside on click")
            val email: String = emailFld.text.toString();
            val password: String = passwordFld.text.toString();
            if(isValidSignInDetails(email, password))
            {
                login(email,password)
            }
            else
            {
                showToast("Invalid email or password")
            }
        }

        btnRegister.setOnClickListener()
        {
            bindingRegister = FragmentRegisterBinding.inflate(layoutInflater)
            val fragmentViewRegister = bindingRegister.root

            // clear fields
            emailFld.text.clear()
            passwordFld.text.clear()

            findViewById<FrameLayout>(R.id.auth_holder).removeAllViews()
            findViewById<FrameLayout>(R.id.auth_holder).addView(fragmentViewRegister)

            val btnRegSent = bindingRegister.btnRegisterR;
            val emailFldR = bindingRegister.emailR;
            val usernameFldR = bindingRegister.usernameR;
            val passwordFldR = bindingRegister.passwordR;
            val btnBack = bindingRegister.btnBackR;

            btnRegSent.setOnClickListener()
            {
                val email: String = emailFldR.text.toString();
                val username: String = usernameFldR.text.toString();
                val password: String = passwordFldR.text.toString();

                if(isValidSignUpDetails(email, username, password))
                {
                    register(email,username,password)

                    // clear fields
                    emailFldR.text.clear()
                    usernameFldR.text.clear()
                    passwordFldR.text.clear()

                }
                else
                {
                    showToast("Invalid email, username or password")
                }
            }

            btnBack.setOnClickListener()
            {
                val fragmentViewBack = bindingLogin.root

                // clear fields
                emailFldR.text.clear()
                usernameFldR.text.clear()
                passwordFldR.text.clear()
                findViewById<FrameLayout>(R.id.auth_holder).removeAllViews()
                findViewById<FrameLayout>(R.id.auth_holder).addView(fragmentViewBack)
            }

        }



    }

    private fun login(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful) {
                prefHandler.putBoolean(Constants.KEY_IS_SIGNED_IN,true)
                prefHandler.putString(Constants.KEY_USER_ID,it.result.user?.uid!!)
                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            }
            else
            {
                showToast("Unable to Sign In")
            }
        }
    }

    // register, but save username in firebase realtime database, and email and password in firebase auth
    private fun register(email: String, username: String, password: String) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful) {
                fire = FirebaseFirestore.getInstance()
                // use the model class User
                val user = User(auth.currentUser!!.uid, username, email, "", false)

                //POST: save user entry in firestore
                fire.collection(Constants.KEY_COLLECTION_USER).document(auth.currentUser!!.uid).set(user).addOnSuccessListener {
                    showToast("User Registered")
                }.addOnFailureListener {
                    showToast("Unable to save user info in cloud")
                }
                val fragmentViewBack = bindingLogin.root
                findViewById<FrameLayout>(R.id.auth_holder).removeAllViews()
                findViewById<FrameLayout>(R.id.auth_holder).addView(fragmentViewBack)
            }
            else
            {
                showToast("Unable to Register")
            }
        }
    }

    private fun isValidSignInDetails(email: String, password: String): Boolean {
        return if(email.trim().isEmpty()) {
            showToast("Enter Email")
            false
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter Valid Email")
            false
        } else if(password.trim().isEmpty()) {
            showToast("Enter Password")
            false
        } else {
            true
        }
    }

    private fun isValidSignUpDetails(email: String, username: String, password: String): Boolean {
        return if(email.trim().isEmpty()) {
            showToast("Enter Email")
            false
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter Valid Email")
            false
        } else if(username.trim().isEmpty()) {
            showToast("Enter Username")
            false
        } else if(password.trim().isEmpty()) {
            showToast("Enter Password")
            false
        } else {
            true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}