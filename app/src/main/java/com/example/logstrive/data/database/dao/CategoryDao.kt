package com.example.logstrive.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.logstrive.data.entity.Category

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
