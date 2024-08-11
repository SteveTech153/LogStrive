package com.example.logstrive.util

object Validator {
    fun isValidUsername(username: String): Boolean{
        val usernameRegex = "^[a-zA-Z0-9]{6,12}$".toRegex()
        return username.matches(usernameRegex)
    }
    fun isPasswordValid(password: String): Boolean{
        if (password.length !in 6..15) return false
        val hasLowercase = password.any { it.isLowerCase() }
        val hasUppercase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        return hasLowercase && hasUppercase && hasDigit && hasSpecialChar
    }
}