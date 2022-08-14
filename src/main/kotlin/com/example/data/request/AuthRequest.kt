package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val name: String,
    val surname: String,
    val username: String,
    val password: String
    // val coins: Long?,
    // val rubies: Long?
)
