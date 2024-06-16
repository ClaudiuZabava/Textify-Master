package com.example.textify.repos

import android.content.Context
import android.util.Log
import com.example.textify.models.ChatRoom
import com.example.textify.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ChatroomsRepo(private val context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)

    fun getChatsFromFirestore(userId: String, callback: (List<ChatRoom>) -> Unit) {
        val receiverQuery = firestore.collection(Constants.KEY_COLLECTION_CHAT_ROOMS).whereEqualTo(Constants.KEY_RECEIVER_ID, userId)
        val senderQuery = firestore.collection(Constants.KEY_COLLECTION_CHAT_ROOMS).whereEqualTo(Constants.KEY_SENDER_ID, userId)
       receiverQuery.get()
            .addOnSuccessListener { result ->
                val chatList = mutableListOf<ChatRoom>()
                for (document in result) {
                    val chat = document.toObject(ChatRoom::class.java)
                    chatList.add(chat)
                }
                senderQuery.get()
                    .addOnSuccessListener { result2 ->
                        for (document in result2) {
                            val chat = document.toObject(ChatRoom::class.java)
                            chatList.add(chat)
                        }
                        Log.d("DEBUG1911", "Inside senderQuery")
                        callback(chatList)
                    }
                    .addOnFailureListener { exception ->
                        Log.d("DEBUG1911", "Error getting chats from senderQuery")
                        callback(chatList)
                    }
            }
            .addOnFailureListener { exception ->
                Log.d("DEBUG1911", "Error getting chats from receiverQuery")
                callback(emptyList())
            }
    }
}