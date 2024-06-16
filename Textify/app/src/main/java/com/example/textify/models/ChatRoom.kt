package com.example.textify.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.textify.utils.Converters
import com.google.firebase.Timestamp
import java.util.Date

@Entity(tableName = "chat_rooms")
@TypeConverters(Converters::class)
data class ChatRoom(
    @PrimaryKey var id: String = "",
    var receiver_id: String = "",
    var receiver_name: String = "",
    var receiver_image: String = "",
    var receiver_activity: String = "",
    var receiver_thoughts: String = "",
    var last_text: String = "",
    var last_text_from: String = "",
    var last_msg_id: String = "",
    var timestamp: Timestamp = Timestamp(Date()),
    var date_added: Timestamp = Timestamp(Date()),
    var message_number: Long = 0,
    var sender_id: String = "",
    var sender_name: String = "",
    var sender_image: String = "",
    var sender_activity: String = "",
    var sender_thoughts: String = "",
    var unread_count: Long = 0
)
