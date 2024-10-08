package com.example.logstrive.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Locale

@Entity(
    tableName = "category",
    indices =  [Index( value = ["category_name"], unique = true)]
)
data class Category(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "category_id") val categoryId: Int = 0,
    @ColumnInfo(name = "category_name") val categoryName: String
){
    override fun toString(): String {
        return categoryName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}