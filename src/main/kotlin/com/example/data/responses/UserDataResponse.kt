package com.example.data.responses

@kotlinx.serialization.Serializable
data class UserDataResponse(
    val name: String,
    val surname: String,
    val username: String,
    val coins: Long?,
    val rubies: Long?,
    val chats: List<String>?
)
