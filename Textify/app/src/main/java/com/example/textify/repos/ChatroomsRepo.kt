package com.example.textify.repos

import android.content.Context
import android.util.Log
import com.example.textify.database.AppDatabase
import com.example.textify.models.ChatRoom
import com.example.textify.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatroomsRepo(private val context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val db = AppDatabase.getDatabase(context)
    private val chatRoomDao = db.chatRoomDao()
    private val scope = CoroutineScope(Dispatchers.IO)
    private var receiverListener: ListenerRegistration? = null
    private var senderListener: ListenerRegistration? = null


    fun syncChatsFromFirestore(userId: String, callback: (List<ChatRoom>) -> Unit) {
        try {
            val receiverQuery = firestore.collection(Constants.KEY_COLLECTION_CHAT_ROOMS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, userId)
            val senderQuery = firestore.collection(Constants.KEY_COLLECTION_CHAT_ROOMS)
                .whereEqualTo(Constants.KEY_SENDER_ID, userId)

            // Listener for receiver query
            receiverListener = receiverQuery.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("DEBUG1911", "Error getting chats from receiverQuery", e)
                    callback(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val chatList = mutableListOf<ChatRoom>()
                    for (document in snapshot.documents) {
                        val chat = document.toObject(ChatRoom::class.java)
                        chat?.let {
                            chatList.add(it)
                            scope.launch {
                                try {
                                    chatRoomDao.insertChatRoom(it)
                                } catch (e: Exception) {
                                    Log.e(
                                        "ChatroomsRepo",
                                        "Error inserting chat into DB: ${e.message}",
                                        e
                                    )
                                }
                            }
                        }
                    }
                    fetchSenderChats(senderQuery, chatList, callback)
                }
            }
        } catch (e: Exception) {
            Log.e("ChatroomsRepo", "Error syncing chats from Firestore: ${e.message}", e)
        }
    }

    private fun fetchSenderChats(senderQuery: Query, chatList: MutableList<ChatRoom>, callback: (List<ChatRoom>) -> Unit) {
        try {
            // Listener for sender query
            senderListener = senderQuery.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("DEBUG1911", "Error getting chats from senderQuery", e)
                    callback(chatList)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    for (document in snapshot.documents) {
                        val chat = document.toObject(ChatRoom::class.java)
                        chat?.let {
                            chatList.add(it)
                            scope.launch {
                                try {
                                    chatRoomDao.insertChatRoom(it)
                                } catch (e: Exception) {
                                    Log.e(
                                        "ChatroomsRepo",
                                        "Error inserting chat into DB: ${e.message}",
                                        e
                                    )
                                }
                            }
                        }
                    }
                    callback(chatList)
                }
            }
        } catch (e: Exception) {
            Log.e("ChatroomsRepo", "Error fetching sender chats: ${e.message}", e)
        }
    }

    fun loadChatsFromDb(callback: (List<ChatRoom>) -> Unit) {
        scope.launch {
            try {
                val chatList = chatRoomDao.getAllChatRooms()
                withContext(Dispatchers.Main) {
                    callback(chatList)
                }
            } catch (e: Exception) {
                Log.e("ChatroomsRepo", "Error loading chats from DB: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    callback(emptyList())
                }
            }
        }
    }

    fun removeListeners() {
        receiverListener?.remove()
        senderListener?.remove()
    }
}