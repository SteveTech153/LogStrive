package com.example.logstrive

import android.app.Application
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MyApp : Application() {
    val context: Context = this
    lateinit var  database: AppDatabase
    lateinit var userRepository: UserRepository
    lateinit var habitRepository: HabitRepository

    override fun onCreate() {
        super.onCreate()
        database =  AppDatabase.getDatabase(this)
        userRepository = UserRepository(database.userDao())
        habitRepository = HabitRepository(database.habitDao(), database.categoryDao(), database.habitLogDao())
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

//            habitRepository.clearAllLogsOfUserOn(2, convertDateToLong(getYesterdayDate()))
//            habitRepository.clearAllLogsOfUserOn(2, convertDateToLong(Date()))

//            habitRepository.insertHabitLog(HabitLog(userId = 2, habitId = 17, date = convertDateToLong(getYesterdayDate()), duration = 50))

            //println("users ${database.userDao().getAllUsers()}")

            //println("Habits My app  ${database.habitDao().getAllHabitsForUser(2).value}")
//            database.habitLogDao().insertOrUpdateHabitLog(HabitLog( userId =  2, date =  Date(), habitId = 17, duration = 30))
//            database.habitLogDao().insertOrUpdateHabitLog(HabitLog( userId =  2, date =  Date(), habitId = 18, duration = 20))
//            database.habitLogDao().insertOrUpdateHabitLog(HabitLog( userId =  2, date =  Date(), habitId = 19, duration = 10))
//            database.habitLogDao().insertOrUpdateHabitLog(HabitLog( userId =  2, date =  Date(), habitId = 22, duration = 120))
//            delay(500)
//            println("OOOPs ${database.habitLogDao().getHabitLogsForUserOn(2, Date()).value}")
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