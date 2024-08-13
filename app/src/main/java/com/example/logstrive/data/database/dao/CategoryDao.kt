package com.example.logstrive.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.logstrive.data.entity.Category

@Dao
interface CategoryDao {
    @Insert( onConflict = OnConflictStrategy.IGNORE )
    suspend fun insertAll(categories: List<Category>)

    @Query("SELECT COUNT(*) FROM category")
    suspend fun getCategoriesCount(): Int

    @Query("SELECT * FROM category")
    fun getAllCategories(): LiveData<List<Category>>
}