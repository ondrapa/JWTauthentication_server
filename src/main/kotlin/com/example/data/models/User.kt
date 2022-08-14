package com.example.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val name: String,
    val surname: String,
    val username: String,
    val password: String,
    val coins: Long?,
    val rubies: Long?,
    val chats: List<String>?,
    val salt: String,
    @BsonId val id: ObjectId = ObjectId()
)
