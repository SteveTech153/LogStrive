package com.example.logstrive

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT * FROM habit WHERE user_id = :userId")
    fun getAllHabitsForUser(userId: Int): Flow<List<Habit>>

    @Query("DELETE FROM habit WHERE user_id = :userId")
    fun clearAllHabitsForUser(userId: Int)

}
