package com.example.logstrive

import android.app.Application
import com.example.logstrive.data.database.AppDatabase
import com.example.logstrive.data.entity.Category
import com.example.logstrive.data.entity.DailyLog
import com.example.logstrive.data.entity.HabitLog
import com.example.logstrive.data.repository.HabitRepository
import com.example.logstrive.data.repository.UserRepository
import com.example.logstrive.util.CategoriesConstant
import com.example.logstrive.util.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Year
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

class MyApp : Application() {
    private lateinit var database: AppDatabase
    lateinit var userRepository: UserRepository
    lateinit var habitRepository: HabitRepository

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        userRepository = UserRepository(database.userDao())
        habitRepository = HabitRepository(database.habitDao(), database.categoryDao(), database.habitLogDao(), database.dailyLogDao())
        insertInitialCategories()
    }

    private fun insertInitialCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            val categoriesCount = database.categoryDao().getCategoriesCount()
            if (categoriesCount == 0) {
                val categories = listOf(
                    Category(categoryId = 1, categoryName = CategoriesConstant.EXERCISE),
                    Category(categoryId = 2, categoryName = CategoriesConstant.SKILL),
                    Category(categoryId = 3, categoryName = CategoriesConstant.EDUCATION),
                    Category(categoryId = 4, categoryName = CategoriesConstant.MEDITATION),
                    Category(categoryId = 5, categoryName = CategoriesConstant.ENTERTAINMENT),
                    Category(categoryId = 6, categoryName = CategoriesConstant.OTHER)
                )
                database.categoryDao().insertAll(categories)
                println("categories count ${database.categoryDao().getCategoriesCount()}")
            } else {
                println("Categories already inserted, count: $categoriesCount")
            }


//            userRepository.updateUserAccountCreatedDate(4, getTimeMillis(2024, 2, 1))
            launch {
//                val calendar = Calendar.getInstance().apply {
//                    set(Calendar.YEAR, 2024)
//                    set(Calendar.MONTH, 6)
//                    set(Calendar.DAY_OF_MONTH, 23)
//                    set(Calendar.HOUR_OF_DAY, 0)
//                    set(Calendar.MINUTE, 0)
//                    set(Calendar.SECOND, 0)
//                    set(Calendar.MILLISECOND, 0)
//                }
//                habitRepository.clearAllUserHabitLogs(4)
//                generateRandomEntries()
                habitRepository.insertHabitLog(HabitLog(userId = 4, habitId = 10, date = Helper.convertDateToLong(Helper.getYesterdayDate()), duration = 20, timestamp = Helper.convertDateToLong(Helper.getYesterdayDate())))
                habitRepository.insertHabitLog(HabitLog(userId = 4, habitId = 11, date = Helper.convertDateToLong(Helper.getYesterdayDate()), duration = 20, timestamp = Helper.convertDateToLong(Helper.getYesterdayDate())))
                habitRepository.insertHabitLog(HabitLog(userId = 4, habitId = 12, date = Helper.convertDateToLong(Helper.getYesterdayDate()), duration = 20, timestamp = Helper.convertDateToLong(Helper.getYesterdayDate())))
                habitRepository.insertHabitLog(HabitLog(userId = 4, habitId = 13, date = Helper.convertDateToLong(Helper.getYesterdayDate()), duration = 20, timestamp = Helper.convertDateToLong(Helper.getYesterdayDate())))
                habitRepository.insertDailyLog(DailyLog(userId = 4, date = Helper.convertDateToLong(Helper.getYesterdayDate()), summary = "Preetttyyy okayy day. lorem ipsum dolor amei solo lorem ipsum", overallMood = 2))
            }
            //habitRepository.insertHabitLog(HabitLog(userId = 1, habitId = 1, date = getTimeMillis(2024, 2, 1), duration = 20, timestamp =  getTimeMillis(2024, 2, 1)) )


        }
    }

    suspend fun generateRandomEntries() {
        val userId = 4
        val habitIds = listOf(10,11,12,13)
        val startDate = Calendar.getInstance().apply {
            set(2024, 2, 2) // March 2, 2024
        }
        val endDate = Calendar.getInstance().apply {
            set(2024, 6, 31) // July 31, 2024
        }

        val random = Random.Default
        var currentDate = startDate

        while (!currentDate.after(endDate)) {
            val day = currentDate.get(Calendar.DAY_OF_MONTH)
            val month = currentDate.get(Calendar.MONTH)
            val year = currentDate.get(Calendar.YEAR)

            val numEntries = random.nextInt(1, 5) // Number of habits to log for the day (1 to 4)
            val selectedHabits = habitIds.shuffled().take(numEntries)

            selectedHabits.forEach { habitId ->
                val duration = random.nextInt(1, 61) // Duration in minutes (1 to 60)
                val dateMillis = getTimeMillis(year, month, day)
                val timestamp = getTimeMillis(year, month, day)

                val habitLog = HabitLog(userId= userId, habitId =  habitId, date =  dateMillis, duration =  duration, timestamp =  timestamp)
                habitRepository.insertHabitLog(HabitLog(userId = habitLog.userId, habitId = habitLog.habitId, date = habitLog.date, duration = habitLog.duration, timestamp = habitLog.timestamp))
            }

            currentDate.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    fun getTimeMillis(year: Int, month: Int, day: Int): Long{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}