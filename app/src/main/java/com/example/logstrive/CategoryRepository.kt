package com.example.logstrive

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    suspend fun insertAll(categories: List<Category>) {
        categoryDao.insertAll(categories)
    }

    suspend fun getCategoriesCount(): Int {
        return categoryDao.getCategoriesCount()
    }

    fun getAllCategories(): LiveData<List<Category>> {
        return categoryDao.getAllCategories()
    }

}
