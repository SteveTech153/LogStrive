package com.example.logstrive.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "daily_log",
    indices = [Index(value = ["user_id", "date"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DailyLog(
    @PrimaryKey(autoGenerate = true) val logId: Int = 0,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "summary") val summary: String,
    @ColumnInfo(name = "overall_mood") val overallMood: String
)

