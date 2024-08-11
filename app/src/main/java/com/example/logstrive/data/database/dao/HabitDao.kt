package com.example.logstrive.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.logstrive.data.entity.Habit

@Dao
interface HabitDao {
    @Insert
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT habit_name FROM habit WHERE user_id = :userId")
    suspend fun getAllHabitNames(userId: Int): List<String>

    @Query("SELECT * FROM habit WHERE user_id = :userId")
    fun getAllHabitsForUser(userId: Int): LiveData<List<Habit>>

    @Query("DELETE FROM habit WHERE user_id = :userId")
    suspend fun clearAllHabitsForUser(userId: Int)

    @Query("SELECT COUNT(*) FROM habit WHERE user_id = :userId")
    suspend fun getHabitsCountOfUser(userId: Int): Int
}
