package com.example.logstrive.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Locale

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0,
    @ColumnInfo(name = "category_name") val categoryName: String
){
    override fun toString(): String {
        return categoryName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}
