package com.example.logstrive.presentation.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.logstrive.R

class HeatmapView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var numberOfBoxes = 0
    private var greenPositions: List<Int> = listOf()
    private var startMonthYear: String = ""
    private var endMonthYear: String = ""
    private val boxSize = 50
    private val spacing = 10

    private val greenPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.graph_item_purple)
    }

    private val grayPaint = Paint().apply {
        color = Color.GRAY
    }

    private val textPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.ligtcement)
        textSize = 40f
    }

    fun setHeatmapData(numberOfBoxes: Int, greenPositions: List<Int>, startMonthYear: String, endMonthYear: String) {
        this.numberOfBoxes = numberOfBoxes
        this.greenPositions = greenPositions
        this.startMonthYear = startMonthYear
        this.endMonthYear = endMonthYear
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val totalWidth = (numberOfBoxes / 7) * (boxSize + spacing)
        val desiredWidth = totalWidth + paddingLeft + paddingRight
        val desiredHeight = (7 * (boxSize + spacing)) + paddingTop + paddingBottom + 50
        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText(startMonthYear, 0f, 40f, textPaint)
        val textWidth = textPaint.measureText(endMonthYear)
        canvas.drawText(endMonthYear, width - textWidth, 40f, textPaint)
        for (i in 0 until numberOfBoxes) {
            val paint = if (i in greenPositions) greenPaint else grayPaint
            val row = i % 7
            val col = i / 7
            val left = col * (boxSize + spacing)
            val top = row * (boxSize + spacing) + 50
            canvas.drawRect(left.toFloat(), top.toFloat(), (left + boxSize).toFloat(), (top + boxSize).toFloat(), paint)
        }
    }
}