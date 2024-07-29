package com.example.logstrive

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertAll(categories: List<Category>)

    @Query("SELECT COUNT(*) FROM category")
    fun getCategoriesCount(): Int

    @Query("SELECT * FROM category")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT categoryId FROM category WHERE category_name = :categoryName")
    suspend fun getIdOfACategory(categoryName: String): Int
}
