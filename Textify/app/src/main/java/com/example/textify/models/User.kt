package com.example.textify.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val image: String = "",
    val online_status: Boolean = false
)
