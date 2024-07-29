package com.example.logstrive

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun getUserByUsername(username: String): User
}
