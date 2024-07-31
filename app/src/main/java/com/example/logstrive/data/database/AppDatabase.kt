package com.example.logstrive.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.logstrive.data.entity.Category
import com.example.logstrive.data.database.dao.CategoryDao
import com.example.logstrive.util.Converters
import com.example.logstrive.data.entity.DailyLog
import com.example.logstrive.data.database.dao.DailyLogDao
import com.example.logstrive.data.entity.Habit
import com.example.logstrive.data.database.dao.HabitDao
import com.example.logstrive.data.entity.HabitLog
import com.example.logstrive.data.database.dao.HabitLogDao
import com.example.logstrive.data.entity.User
import com.example.logstrive.data.database.dao.UserDao

@Database(
    entities = [User::class, Category::class, Habit::class, DailyLog::class, HabitLog::class],
    version = 1,
//    autoMigrations = [AutoMigration(1, 2)],
//    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun habitDao(): HabitDao
    abstract fun dailyLogDao(): DailyLogDao
    abstract fun habitLogDao(): HabitLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
//    class Migration_1_2 : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("ALTER TABLE habit_log ADD COLUMN timestamp INTEGER NOT NULL DEFAULT 0")
//            database.execSQL("DROP INDEX IF EXISTS index_habit_log_user_id_habit_id_date")
//            database.execSQL("CREATE UNIQUE INDEX index_habit_log_user_id_habit_id_date_timestamp ON habit_log (user_id, habit_id, date, timestamp)")
//        }
//    }

}
