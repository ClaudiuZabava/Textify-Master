package com.example.textify.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val email: String = "",
    val image: String = "",
    val status: String = "I'm using Textify!",
    val online_status: Boolean = false
)
