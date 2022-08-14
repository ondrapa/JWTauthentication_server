package com.example.data.dataSource

import com.example.data.models.User
import org.bson.types.ObjectId

interface UserDataSource {
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserById(id: ObjectId): User?
    suspend fun insertUser(user: User): Boolean
}