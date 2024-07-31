package com.example.logstrive.data.repository

import androidx.lifecycle.LiveData
import com.example.logstrive.data.database.dao.CategoryDao
import com.example.logstrive.data.entity.Category

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
