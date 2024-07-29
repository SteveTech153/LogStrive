package com.example.logstrive

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val habitRepository by lazy { HabitRepository(database.habitDao(), database.categoryDao()) }

    override fun onCreate() {
        super.onCreate()
        insertInitialCategories()
    }
    private fun insertInitialCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            if (database.categoryDao().getAllCategories().value?.isEmpty() == true) {
                val categories = listOf(
                    Category(categoryId = 1, categoryName = "exercise"),
                    Category(categoryId = 2, categoryName = "skill"),
                    Category(categoryId = 3, categoryName = "education"),
                    Category(categoryId = 4, categoryName = "meditation"),
                    Category(categoryId = 5, categoryName = "entertainment"),
                    Category(categoryId = 6, categoryName = "other")
                )
                database.categoryDao().insertAll(categories)
            }
        }
    }


}