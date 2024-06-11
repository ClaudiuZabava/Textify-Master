package com.example.textify.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.textify.R
import com.example.textify.databinding.FragmentRegisterBinding
import com.example.textify.models.User
import com.example.textify.utils.Constants
import com.example.textify.utils.PreferenceHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment:Fragment() {

    private lateinit var bindingRegister: FragmentRegisterBinding;
    private lateinit var prefHandler: PreferenceHandler;
    private lateinit var auth: FirebaseAuth;
    private lateinit var fire: FirebaseFirestore;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingRegister = FragmentRegisterBinding.inflate(layoutInflater)
        prefHandler = PreferenceHandler(requireContext())
        return bindingRegister.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners()
    {
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

            }
            else
            {
                showToast("Invalid email, username or password")
            }
        }

        btnBack.setOnClickListener()
        {
            emailFldR.text.clear()
            usernameFldR.text.clear()
            passwordFldR.text.clear()

            requireActivity().supportFragmentManager.popBackStack()
        }

    }

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

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.auth_holder, LoginFragment())
                    .commit()
            }
            else
            {
                showToast("Unable to Register. User already exists")
            }
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
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

}