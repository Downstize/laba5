package com.example.laba5.presentation.adapter

import java.io.Serializable

data class User (
    val name: String,
    val email: String,
    val password: String
) : Serializable
