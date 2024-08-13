package com.example.logstrive.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit_log",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["habit_id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HabitLog(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "habit_log_id") val habitLogId: Int = 0,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "habit_id") val habitId: Int,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "duration") val duration: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)
