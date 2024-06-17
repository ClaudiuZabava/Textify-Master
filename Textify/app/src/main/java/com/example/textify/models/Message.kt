package com.example.textify.models

data class Message(
    val id: String,
    val chat_room_id: String, // This will link the message to its chat room
    val selected: Boolean,
    val datetime: String?,
    val from_id: String?,
    val imageUrl: String?,
    val reply_attached: Boolean,
    val reply_id: String?,
    val reply_pos: Int,
    val reply_text: String?,
    val reply_to: String?,
    val status: String?,
    val text: String?,
    val timestamp: Long,
    val type: String?
)
