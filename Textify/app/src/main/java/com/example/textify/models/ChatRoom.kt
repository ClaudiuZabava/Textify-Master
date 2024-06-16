package com.example.textify.models

import com.google.firebase.Timestamp
import java.util.Date

data class ChatRoom(
    var id: String? = "",
    var receiver_id: String? = "",
    var receiver_name: String? = "",
    var receiver_image: String? = "",
    var receiver_activity: String? = "",
    var receiver_thoughts: String? = "",
    var last_text: String? = "",
    var last_text_from: String? = "",
    var last_msg_id: String? = "",
    var timestamp: Timestamp? = Timestamp(Date()),
    var date_added: Timestamp? = Timestamp(Date()),
    var message_number: Long = 0,
    var sender_id: String? = "",
    var sender_name: String? = "",
    var sender_image: String? = "",
    var sender_activity: String? = "",
    var sender_thoughts: String? = "",
    var unread_count: Long = 0
)
