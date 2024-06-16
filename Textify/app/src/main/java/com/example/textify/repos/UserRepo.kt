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

    fun syncWithFirestore() {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { result ->
                scope.launch {
                    for (document in result) {
                        val user = document.toObject(User::class.java)
                        userDao.insert(user) // This will replace the existing user or insert a new one
                    }
                }
            }
    }

    fun uploadUserToFirestore(user: User) {
        Log.d("DEBUG", "Uploading user to firestore - AICI")
        firestore.collection(Constants.KEY_COLLECTION_USER).document(user.id).set(user)
            .addOnSuccessListener {
                syncWithFirestore()
                showToast("User Registered ")
            }.addOnFailureListener{
                showToast("Failed to register user")
            }

    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}