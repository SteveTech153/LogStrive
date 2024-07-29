package com.example.logstrive

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class HabitRepository(private val habitDao: HabitDao, private val categoryDao: CategoryDao) {

    fun getAllHabitsForUser(userId: Int): Flow<List<Habit>> {
        return habitDao.getAllHabitsForUser(userId)
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

    fun getAllCategories(): LiveData<List<Category>> = categoryDao.getAllCategories()

    suspend fun getIdOfACategory(categoryName: String): Int {
        return categoryDao.getIdOfACategory(categoryName)
    }
}
