package com.example.logstrive.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logstrive.util.SessionManager
import com.example.logstrive.data.entity.User
import com.example.logstrive.data.repository.UserRepository
import com.example.logstrive.util.Helper
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import java.util.Date

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    fun signup(context: Context, username: String, password: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val existingUser = repository.getUserByUsername(username)
            if (existingUser == null) {
                val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
                repository.insertUser(User(username = username, password = hashedPassword, accountCreatedDate =  Helper.convertDateToLong(Date())) )
                val userId = repository.getUserByUsername(username).userId

                userId.let { SessionManager.login(context, username, userId) }

                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun login(context: Context, username: String, password: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username)
            if (user != null && BCrypt.checkpw(password, user.password)) {
                SessionManager.login(context, user.username, user.userId)
                callback(true)
            } else {
                callback(false)
            }
        }
    }
    fun logout(context: Context) {
        viewModelScope.launch {
            SessionManager.logout(context)
        }
    }

    fun updateUsername(context: Context, newUsername: String, userId: Int, callback: (Boolean) -> Unit){
        viewModelScope.launch {
            val existingUser = repository.getUserByUsername(newUsername)
            if (existingUser == null) {
                repository.updateUsername(newUsername, userId)
                SessionManager.updateUsername(context, newUsername)
                callback(true)
            }else{
                callback(false)
            }
        }
    }

    suspend fun getAccountCreatedDate(userId: Int): Long{
        return repository.getAccountCreatedDate(userId)
    }

    fun isUsernameValid(username: String): Boolean {
        val usernameRegex = "^[a-zA-Z0-9]{6,12}$".toRegex()
        return username.matches(usernameRegex)
    }

    fun isPasswordValid(password: String): Boolean {
        if (password.length !in 6..15) return false
        val hasLowercase = password.any { it.isLowerCase() }
        val hasUppercase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        return hasLowercase && hasUppercase && hasDigit && hasSpecialChar
    }
}
