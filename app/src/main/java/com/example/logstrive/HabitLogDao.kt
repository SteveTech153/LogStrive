package com.example.logstrive

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

@Dao
interface HabitLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habitLog: HabitLog)

    @Query("SELECT * FROM habit_log WHERE user_id = :userId AND habit_id = :habitId AND date = :date")
    suspend fun getAHabitLog(userId: Int, habitId: Int, date: Long): HabitLog?

    @Query("SELECT * FROM habit_log WHERE user_id = :userId AND habit_id = :habitId")
    suspend fun getHabitLogsForUser(userId: Int, habitId: Int): List<HabitLog>

    @Query("SELECT * FROM habit_log WHERE user_id = :userId AND date = :date")
    fun getHabitLogsForUserOn(userId: Int, date: Long): LiveData<List<HabitLog>>

    @Query("DELETE FROM habit_log WHERE user_id = :userId AND date = :date")
    fun clearAllLogsOfUserOn(userId: Int, date: Long)
}
