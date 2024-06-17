package com.example.textify.repos

import android.content.Context
import android.util.Log
import com.example.textify.database.AppDatabase
import com.example.textify.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.Toast
import com.example.textify.utils.Constants

class UserRepo(private val context: Context) {

    private val userDao = AppDatabase.getDatabase(context).userDao()
    private val firestore = FirebaseFirestore.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)

    fun insert(user: User) {
        scope.launch {
            userDao.insert(user)
        }
    }

    fun update(user: User) {
        scope.launch {
            userDao.update(user)
        }
    }

    suspend fun updateUsername(uid: String, username: String) {
        userDao.updateUsername(uid, username)
    }

    suspend fun updateOnlineStatus(uid: String, status: Boolean) {
        userDao.updateOnlineStatus(uid, status)
    }

    suspend fun updateImage(uid: String, image: String) {
        userDao.updateImage(uid, image)
    }

    suspend fun updateStatus(uid: String, status: String) {
        userDao.updateStatus(uid, status)
    }


    fun updateFieldFirestore(uid: String, field: String, value: Any) {
        Log.d("DEBUG1911", "Updatiing the field")
        firestore.collection("users").document(uid).update(field, value)
            .addOnSuccessListener {
                // Update local database based on field
                scope.launch {
                    when (field) {
                        "name" -> updateUsername(uid, value as String)
                        "online_status" -> updateOnlineStatus(uid, value as Boolean)
                        "image" -> updateImage(uid, value as String)
                        "status" -> updateStatus(uid, value as String)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.d("DEBUG1911", "Failed to update field: $e")
            }
    }


    suspend fun getUser(uid: String): User? {
        return userDao.getUser(uid)
    }

    fun deleteAllUsers() {
        scope.launch {
            userDao.deleteAllUsers()
        }
    }

    // sync only a user mentioned by id
    fun syncWithFirestore(uid: String) {
        firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { result ->
                Log.d("DEBUG1911", "Success result is $result")
                scope.launch {
                    val user = result.toObject(User::class.java)
                    Log.d("DEBUG1911", "User is $user")
                    val localUser = userDao.getUser(user!!.id)
                    Log.d("DEBUG1911", "Local user is $localUser")

                    if(localUser == null) {
                        deleteAllUsers()
                    }
                    userDao.insert(user) // This will replace the existing user or insert a new one
                }
            }
            .addOnFailureListener() {
                Log.d("DEBUG1911", "Failed to get user from firestore")
            }
    }
    fun uploadUserToFirestore(user: User) {
        Log.d("DEBUG", "Uploading user to firestore - AICI")
        firestore.collection(Constants.KEY_COLLECTION_USER).document(user.id).set(user)
            .addOnSuccessListener {
                syncWithFirestore(user.id)
                showToast("User Registered ")
            }.addOnFailureListener{
                showToast("Failed to register user")
            }

    }

    // Function to get user ID from db

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}