package com.example.logstrive

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

@Dao
interface HabitLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateHabitLog(habitLog: HabitLog)

    @Query("SELECT * FROM habit_log WHERE user_id = :userId AND habit_id = :habitId AND date = :date")
    suspend fun getHabitLog(userId: Int, habitId: Int, date: Date): HabitLog?

    @Query("SELECT * FROM habit_log WHERE user_id = :userId AND habit_id = :habitId")
    suspend fun getHabitLogsForUser(userId: Int, habitId: Int): List<HabitLog>
}
