package com.example.textify.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.textify.R
import com.example.textify.activity.MainActivity
import com.example.textify.databinding.FragmentLoginBinding
import com.example.textify.repos.ChatroomsRepo
import com.example.textify.repos.UserRepo
import com.example.textify.utils.Constants
import com.example.textify.utils.PreferenceHandler
import com.google.firebase.auth.FirebaseAuth

class LoginFragment:Fragment() {

    private lateinit var bindingLogin: FragmentLoginBinding;
    private lateinit var prefHandler: PreferenceHandler;
    private lateinit var auth: FirebaseAuth;
    private lateinit var userRepo: UserRepo;
    private lateinit var chatroomsRepo: ChatroomsRepo;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingLogin = FragmentLoginBinding.inflate(layoutInflater)
        prefHandler = PreferenceHandler(requireContext())
        return bindingLogin.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRepo = UserRepo(requireContext())
        chatroomsRepo = ChatroomsRepo(requireContext())
        setListeners()
    }

    private fun setListeners() {

        val btnLogin = bindingLogin.btnLogin;
        val emailFld = bindingLogin.email;
        val passwordFld = bindingLogin.password;
        val btnRegister = bindingLogin.btnRegister;

        btnLogin.setOnClickListener()
        {
            if(!isNetworkConnected())
            {
                showToast("No Internet Connection")
                return@setOnClickListener
            }
            else {
                //Log.d("DEBUG ", "Passed here inside on click")
                val email: String = emailFld.text.toString();
                val password: String = passwordFld.text.toString();
                if (isValidSignInDetails(email, password)) {
                    login(email, password)
                } else {
                    showToast("Invalid email or password")
                }
            }
        }
        btnRegister.setOnClickListener()
        {
            if(!isNetworkConnected())
            {
                showToast("No Internet Connection")
                return@setOnClickListener
            }
            else {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.auth_holder, RegisterFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun login(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful) {
                Log.d("DEBUG1911 ", "Passed here inside on click")
                prefHandler.putBoolean(Constants.KEY_IS_SIGNED_IN,true)
                prefHandler.putString(Constants.KEY_USER_ID,it.result.user?.uid!!)
                Log.d("DEBUG1911 ", "Preferences were ok")
                userRepo.updateFieldFirestore(it.result.user?.uid!!,"online_status",true)
                chatroomsRepo.deleteAllChatRooms();
                Log.d("DEBUG1911 ", "Passed the update field firestore")
                val intent = Intent(activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                activity?.finish()
            }
            else
            {
                showToast("Unable to Sign In")
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

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

}