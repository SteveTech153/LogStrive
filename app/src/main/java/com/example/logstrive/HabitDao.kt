package com.example.logstrive

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HabitDao {
    @Insert
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT * FROM habit WHERE user_id = :userId")
    fun getAllHabitsForUser(userId: Int): LiveData<List<Habit>>

    @Query("DELETE FROM habit WHERE user_id = :userId")
    fun clearAllHabitsForUser(userId: Int)

    @Query("SELECT habit_name FROM habit WHERE habitId = :habitId LIMIT 1")
    fun getHabitName(habitId: Int): String

}
