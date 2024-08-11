package com.example.logstrive.data.repository

import androidx.lifecycle.LiveData
import com.example.logstrive.data.database.dao.CategoryDao
import com.example.logstrive.data.database.dao.DailyLogDao
import com.example.logstrive.data.database.dao.HabitDao
import com.example.logstrive.data.database.dao.HabitLogDao
import com.example.logstrive.data.entity.Category
import com.example.logstrive.data.entity.DailyLog
import com.example.logstrive.data.entity.Habit
import com.example.logstrive.data.entity.HabitLog
import com.example.logstrive.data.entity.User

class HabitRepository(private val habitDao: HabitDao, private val categoryDao: CategoryDao, private val habitLogDao: HabitLogDao, private val dailyLogDao: DailyLogDao) {

    fun getAllHabitsForUser(userId: Int): LiveData<List<Habit>> {
        return habitDao.getAllHabitsForUser(userId)
    }

    suspend fun insertHabitLog(habitLog: HabitLog){
        habitLogDao.insert(habitLog)
    }

    suspend fun getHabitsCountOfUser(userId: Int): Int{
        return habitDao.getHabitsCountOfUser(userId)
    }
    suspend fun getAllHabitNames(userId: Int): List<String>{
        return habitDao.getAllHabitNames(userId)
    }
    suspend fun insert(habit: Habit) {
        habitDao.insert(habit)
    }

    suspend fun update(habit: Habit){
        habitDao.update(habit)
    }

    suspend fun delete(habit: Habit){
        habitDao.delete(habit)
    }

    suspend fun clearAllHabitsForUser(userId: Int){
        habitDao.clearAllHabitsForUser(userId)
    }

    suspend fun insertDailyLog(dailyLog: DailyLog){
        dailyLogDao.insertOrUpdateDailyLog(dailyLog)
    }
    suspend fun getDailyLog(userId: Int, date: Long): DailyLog?{
        return dailyLogDao.getDailyLog(userId, date)
    }
    suspend fun clearAllLogsOfUserOn(userId: Int, date: Long){
        habitLogDao.clearAllLogsOfUserOn(userId, date)
    }

    fun getAllCategories(): LiveData<List<Category>> = categoryDao.getAllCategories()

    fun getHabitLogsForUserOnAsLiveData(userId: Int, date: Long): LiveData<List<HabitLog>>{
        return habitLogDao.getHabitLogsForUserOnAsLiveData(userId, date)
    }

    suspend fun getHabitLogsForUserOn(userId: Int, date: Long): List<HabitLog>{
        return habitLogDao.getHabitLogsForUserOn(userId, date)
    }

    fun getAllHabitsOfUser(userId: Int): LiveData<List<Habit>> {
        return habitDao.getAllHabitsForUser(userId)
    }

    suspend fun getAllActiveDatesForHabitOf(habitId: Int, userId: Int): List<Long>? {
        return habitLogDao.getAllActiveDatesForHabitOf(habitId, userId)
    }

    suspend fun clearAllUserHabitLogs(userId: Int){
        return habitLogDao.clearAllUserHabitLogs(userId)
    }

}
