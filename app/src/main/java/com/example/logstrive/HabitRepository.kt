package com.example.logstrive

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import java.util.Calendar
import java.util.Date

class HabitRepository(private val habitDao: HabitDao, private val categoryDao: CategoryDao, private val habitLogDao: HabitLogDao) {

    fun getAllHabitsForUser(userId: Int): LiveData<List<Habit>> {
        return habitDao.getAllHabitsForUser(userId)
    }

    suspend fun insertHabitLog(habitLog: HabitLog){
        habitLogDao.insert(habitLog)
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
    fun clearAllLogsOfUserOn(userId: Int, date: Long){
        habitLogDao.clearAllLogsOfUserOn(userId, date)
    }

    fun getAllCategories(): LiveData<List<Category>> = categoryDao.getAllCategories()

    suspend fun getIdOfACategory(categoryName: String): Int {
        return categoryDao.getIdOfACategory(categoryName)
    }

    suspend fun getHabitLogsForUserOn(userId: Int, date: Long): LiveData<List<HabitLog>>{
        return habitLogDao.getHabitLogsForUserOn(userId, date)
    }

    suspend fun getAllHabitsOfUser(userId: Int): LiveData<List<Habit>> {
        return habitDao.getAllHabitsForUser(userId)
    }

}
