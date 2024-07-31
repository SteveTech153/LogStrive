package com.example.logstrive

import android.app.Application
import com.example.logstrive.data.database.AppDatabase
import com.example.logstrive.data.entity.Category
import com.example.logstrive.data.repository.HabitRepository
import com.example.logstrive.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MyApp : Application() {
    private lateinit var database: AppDatabase
    lateinit var userRepository: UserRepository
    lateinit var habitRepository: HabitRepository

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        userRepository = UserRepository(database.userDao())
        habitRepository = HabitRepository(database.habitDao(), database.categoryDao(), database.habitLogDao())
        insertInitialCategories()
    }

    private fun insertInitialCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            val categoriesCount = database.categoryDao().getCategoriesCount()
            if (categoriesCount == 0) {
                val categories = listOf(
                    Category(categoryId = 1, categoryName = "exercise"),
                    Category(categoryId = 2, categoryName = "skill"),
                    Category(categoryId = 3, categoryName = "education"),
                    Category(categoryId = 4, categoryName = "meditation"),
                    Category(categoryId = 5, categoryName = "entertainment"),
                    Category(categoryId = 6, categoryName = "other")
                )
                database.categoryDao().insertAll(categories)
                println("categories count ${database.categoryDao().getCategoriesCount()}")
            } else {
                println("Categories already inserted, count: $categoriesCount")
            }

            //
//            database.habitLogDao().insert(HabitLog(userId = 1, habitId = 1, date = convertDateToLong(getYesterdayDate()), duration = 30, timestamp = 0))
//            database.habitLogDao().insert(HabitLog(userId = 1, habitId = 2, date = convertDateToLong(getYesterdayDate()), duration = 40, timestamp = 0))
//            database.habitLogDao().insert(HabitLog(userId = 1, habitId = 3, date = convertDateToLong(getYesterdayDate()), duration = 50, timestamp = 0))
//            database.habitLogDao().insert(HabitLog(userId = 1, habitId = 4, date = convertDateToLong(getYesterdayDate()), duration = 10, timestamp = 0))
        }
    }

    fun convertDateToLong(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getYesterdayDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return calendar.time
    }
}