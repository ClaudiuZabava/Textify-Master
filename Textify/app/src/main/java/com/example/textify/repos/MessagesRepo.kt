package com.example.textify.repos

import android.content.Context
import android.util.Log
import com.example.textify.database.AppDatabase
import com.example.textify.models.Message
import com.example.textify.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class MessagesRepo(private val context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private var messageListener: ListenerRegistration? = null

    fun syncMessagesFromFirestore(chatRoomId: String, callback: (List<Message>) -> Unit) {
        try {
            val messagesQuery = firestore.collection(Constants.KEY_COLLECTION_CHAT_ROOMS)
                .document(chatRoomId)
                .collection(Constants.KEY_COLLECTION_CHAT)
                .orderBy("timestamp", Query.Direction.ASCENDING)

            // Listener for messages query
            messageListener = messagesQuery.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("DEBUG", "Error getting messages", e)
                    callback(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val messageList = mutableListOf<Message>()
                    for (document in snapshot.documents) {
                        val message = document.toObject(Message::class.java)
                        message?.let {
                            messageList.add(it)
                        }
                    }
                    callback(messageList)
                }
            }
        } catch (e: Exception) {
            Log.e("MessagesRepo", "Error syncing messages from Firestore: ${e.message}", e)
        }
    }

    fun removeListeners() {
        messageListener?.remove()
    }
}