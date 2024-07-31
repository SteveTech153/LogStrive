package com.example.logstrive.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.logstrive.data.entity.DailyLog
import java.util.Date

@Dao
interface DailyLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyLog(dailyLog: DailyLog)

    @Query("SELECT * FROM daily_log WHERE user_id = :userId AND date = :date")
    suspend fun getDailyLog(userId: Int, date: Date): DailyLog?
}
