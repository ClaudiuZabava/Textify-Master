package com.example.textify.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.textify.R
import com.example.textify.adapters.MessagesAdapter
import com.example.textify.databinding.ActivityChatBinding
import com.example.textify.models.ChatRoom
import com.example.textify.models.Message
import com.example.textify.models.User
import com.example.textify.repos.MessagesRepo
import com.example.textify.utils.Constants
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class ChatActivity : AppCompatActivity() {
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var messagesRepo: MessagesRepo
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatRoomId: String
    private lateinit var fire : FirebaseFirestore
    private lateinit var chatRoom: ChatRoom
    private lateinit var userId: String
    private lateinit var senderId: String
    private lateinit var receiverId: String
    private lateinit var imageUrl: String
    private var replyPos: Long = 0
    private lateinit var bindingChat : ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingChat = ActivityChatBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(bindingChat.root)
        userId = FirebaseAuth.getInstance().uid!!
        val id1 = intent.getStringExtra(Constants.KEY_SENDER_ID)!!
        val id2 = intent.getStringExtra(Constants.KEY_RECEIVER_ID)!!
        chatRoomId = intent.getStringExtra(Constants.KEY_CHAT_ROOM_ID)!!
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if(userId == id1) {
            senderId = id1
            receiverId = id2
        } else {
            senderId = id2
            receiverId = id1
        }

        retrieveInfo()

        recyclerView = bindingChat.chatscreenRvChat
        messagesRepo = MessagesRepo(applicationContext)

        // Initialize MessagesAdapter with empty list initially
        messagesAdapter = MessagesAdapter(this, listOf(), "", "", recyclerView)
        recyclerView.adapter = messagesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch messages from Firestore and update adapter
        messagesRepo.syncMessagesFromFirestore(chatRoomId) { messages ->
            messagesAdapter.updateMessages(messages)
        }

        setLiseners()
    }

    private fun retrieveInfo() {
        fire = FirebaseFirestore.getInstance()

        // Get the chat room details from Firestore
        fire.collection(Constants.KEY_COLLECTION_CHAT_ROOMS)
            .document(chatRoomId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    chatRoom = document.toObject(ChatRoom::class.java)!!

                    // Display the chat room details in the chat activity
                    bindingChat.chatscreenName.text = chatRoom.receiver_name

                    // Set up a real-time listener for the receiver's user info
                    fire.collection(Constants.KEY_COLLECTION_USER)
                        .document(receiverId)
                        .addSnapshotListener { snapshot, error ->
                            if (error != null) {
                                // Handle errors
                                return@addSnapshotListener
                            }

                            if (snapshot != null && snapshot.exists()) {
                                val receiver = snapshot.toObject(User::class.java)!!
                                Log.d("DEBUG1911", "Receiver: $receiver")

                                // Display the receiver's profile picture using Glide
                                Glide.with(this)
                                    .load(receiver.image)
                                    .placeholder(R.drawable.profile_placeholder)
                                    .into(bindingChat.chatscreenProfile)

                                if(receiver.online_status == true) {
                                    bindingChat.chatscreenActivity.text = "Online"
                                    bindingChat.chatscreenStatus.setCardBackgroundColor(resources.getColor(R.color.chatscreen_color_online))
                                } else {
                                    bindingChat.chatscreenActivity.text = "Offline"
                                    bindingChat.chatscreenStatus.setCardBackgroundColor(resources.getColor(R.color.chatscreen_color_offline))
                                }
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("DEBUG1911", "Error getting chat room details: $exception")
            }
    }

    // Function to send a message
    private fun uploadMessage(chat: Message) {
        var unread_count: Long = 0
        var message_number: Long = 0
        fire = FirebaseFirestore.getInstance()
        fire.collection(Constants.KEY_COLLECTION_CHAT_ROOMS).document(chatRoomId).get().addOnSuccessListener {
            val room: ChatRoom = it.toObject(ChatRoom::class.java)!!
            unread_count = room.unread_count + 1
            message_number = room.message_number + 1
            chat.timestamp = message_number
            var chatTimestamp: Timestamp = Timestamp(Date())
            Log.d("DEBUG1911", "Message number: $message_number")
            fire.collection(Constants.KEY_COLLECTION_CHAT_ROOMS).document(chatRoomId).collection(Constants.KEY_COLLECTION_CHAT).add(chat).addOnSuccessListener {
                Log.d("DEBUG1911", "Message sent successfully")
                Log.d("DEBUG1911", "Message ID: ${it.id}")
                Log.d("DEBUG1911", "Message text: ${chat.text}")
                fire.collection(Constants.KEY_COLLECTION_CHAT_ROOMS).document(chatRoomId).update(
                    "last_text",
                    chat.text,
                    "message_number",
                    message_number,
                    "last_text_from",
                    senderId,
                    "timestamp",
                    chatTimestamp,
                    "unread_count",
                    unread_count,
                    "last_msg_id",
                    it.id
                );
            }
        }
    }
    private fun setLiseners() {
        bindingChat.chatscreenBtnSend.setOnClickListener {


            sendMessage()

            bindingChat.chatsreenTexteditMsg.text.clear()
        }

        bindingChat.chatscreenBack.setOnClickListener {
            finish()
        }
    }
    private fun sendMessage(imgUrl: String = "") {
        val text: String = bindingChat.chatsreenTexteditMsg.text.toString().trim()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dt: String = LocalDateTime.now().format(formatter)
        var message_number: Long = 0
        var unread_count: Long = 0
        var replyAttached: Boolean = false
        var replyTo: String = ""
        var replyBy: String = ""
        var replyText:String = ""
        if(bindingChat.replyCv.visibility== View.VISIBLE) {
            replyAttached = true
            replyTo = if(bindingChat.replyName.text.toString()=="You") userId
            else receiverId
            replyBy = userId
            replyText = bindingChat.replyMsg.text.toString()
        }
        val chat = Message("",chatRoomId,false,dt,senderId,imgUrl,replyAttached,replyBy,replyPos,replyText, replyTo,"Sent",text,message_number,"text")
        uploadMessage(chat)

    }
}