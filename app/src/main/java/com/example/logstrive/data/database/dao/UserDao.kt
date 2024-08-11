package com.example.logstrive.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.logstrive.data.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun getUserByUsername(username: String): User

    @Query("UPDATE user SET username = :newUsername WHERE user_id = :userId")
    suspend fun updateUsername(newUsername: String, userId: Int)

    @Query("SELECT account_created_date FROM user WHERE user_id = :userId")
    suspend fun getAccountCreatedDate(userId: Int): Long

    //For testing
    @Query("UPDATE user SET account_created_date = :newDate WHERE user_id = :userId")
    suspend fun updateUserAccountCreatedDate(userId: Int, newDate: Long)
}
