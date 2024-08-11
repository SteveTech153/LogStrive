package com.example.logstrive.data.entity

data class GraphItem(
    val habitName: String,
    val noOfBoxes: Int,
    val activeIndexes: List<Int>,
    val startYearMonth: String,
    val endYearMonth: String
)
