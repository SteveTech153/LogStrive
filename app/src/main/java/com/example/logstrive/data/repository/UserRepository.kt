package com.example.logstrive.data.repository

import com.example.logstrive.data.database.dao.UserDao
import com.example.logstrive.data.entity.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getUserByUsername(username: String): User {
        return userDao.getUserByUsername(username)
    }

    suspend fun updateUsername(newUsername: String, userId: Int){
        userDao.updateUsername(newUsername, userId)
    }

    suspend fun getAccountCreatedDate(userId: Int): Long{
        return userDao.getAccountCreatedDate(userId)
    }

    //testing purpose
    suspend fun updateUserAccountCreatedDate(userId: Int, newDate: Long){
        userDao.updateUserAccountCreatedDate(userId, newDate)
    }
}
