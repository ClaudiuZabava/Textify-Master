package com.example.textify.models

data class Message(
    var id: String = "",
    var chat_room_id: String ="" , // This will link the message to its chat room
    var selected: Boolean = false,
    var datetime: String = "",
    var from_id: String = "",
    var imageUrl: String = "",
    var reply_attached: Boolean = false,
    var reply_id: String = "",
    var reply_pos: Long = 0,
    var reply_text: String = "",
    var reply_to: String = "",
    var status: String = "",
    var text: String = "",
    var timestamp: Long = 0,
    var type: String = "",
)
